package com.dino.plExplorer.service.matches;

import com.dino.plExplorer.dto.external.espn.scoreboard.EspnCompetition;
import com.dino.plExplorer.dto.external.espn.scoreboard.EspnDetail;
import com.dino.plExplorer.dto.external.espn.scoreboard.EspnEvent;
import com.dino.plExplorer.dto.external.espn.scoreboard.EspnScoreboardResponse;
import com.dino.plExplorer.dto.response.matches.MatchDto;
import com.dino.plExplorer.entity.Match;
import com.dino.plExplorer.entity.Player;
import com.dino.plExplorer.entity.Season;
import com.dino.plExplorer.entity.Team;
import com.dino.plExplorer.mapper.matches.FootballDataMatchEntityMapper;
import com.dino.plExplorer.repository.MatchRepository;
import com.dino.plExplorer.repository.PlayerRepository;
import com.dino.plExplorer.repository.SeasonRepository;
import com.dino.plExplorer.repository.TeamRepository;
import com.dino.plExplorer.service.espn.EspnApiService;
import com.dino.plExplorer.service.seasons.SeasonService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class MatchesSyncService {

    private static final DateTimeFormatter ESPN_DATE_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;

    private final MatchesService matchesService;
    private final EspnApiService espnApiService;
    private final SeasonService seasonService;

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final SeasonRepository seasonRepository;
    private final PlayerRepository playerRepository;

    private final FootballDataMatchEntityMapper footballDataMatchEntityMapper;
    private final EspnEventUpsertService espnEventUpsertService;

    // if this method is called for a gameweek that has already been synced, it will update matches and events to reflect any changes in the source data, but will not create duplicates
    // current season is default if seasonStartYearOpt is not provided
    public void syncMatchesForGameweek(int gameweek, Optional<Integer> seasonStartYearOpt) {
        log.info("Syncing matches and events for gameweek {}", gameweek);
        int seasonStartYear = seasonStartYearOpt.orElseGet(() -> seasonService.getCurrentSeason());
        List<MatchDto> matches = matchesService.getMatchesFromFootballDataApi(gameweek, seasonStartYear );
        if (matches.isEmpty()) {
            log.info("No matches found for gameweek {}", gameweek);
            return;
        }

        Season season = seasonRepository.findByStartYear(seasonStartYear)
                .orElse(seasonRepository.findByIsCurrentTrue());

        List<Match> persistedMatches = upsertFootballDataMatches(matches, season);
        if (persistedMatches.isEmpty()) {
            log.warn("No matches persisted for gameweek {}, skipping ESPN sync", gameweek);
            return;
        }

        Optional<LocalDate> minDateOpt = getFirstKickoffTime(matches).map(OffsetDateTime::toLocalDate);
        Optional<LocalDate> maxDateOpt = getLastKickoffTime(matches).map(OffsetDateTime::toLocalDate);
        if (minDateOpt.isEmpty() || maxDateOpt.isEmpty()) {
            log.warn("Could not determine kickoff date range for gameweek {}", gameweek);
            return;
        }

        String startDate = minDateOpt.get().format(ESPN_DATE_FORMAT);
        String endDate = maxDateOpt.get().format(ESPN_DATE_FORMAT);

        Optional<EspnScoreboardResponse> espnResponseOpt = espnApiService.fetchScoreboardForDateRange(startDate, endDate);
        if (espnResponseOpt.isEmpty() || espnResponseOpt.get().getEvents() == null || espnResponseOpt.get().getEvents().isEmpty()) {
            log.warn("No ESPN scoreboard events found for date range {} - {}", startDate, endDate);
            return;
        }

        List<EspnEvent> events = espnResponseOpt.get().getEvents();
        Map<String, Team> teamsByEspnId = loadTeamsByEspnId();
        Map<String, Player> playersByEspnId = loadPlayersByEspnId(events);

        for (EspnEvent event : events) {
            try {
                espnEventUpsertService.upsertEspnEvent(event, persistedMatches, teamsByEspnId, playersByEspnId);
            } catch (Exception ex) {
                log.error("Failed to upsert ESPN event {}. Continuing with next event.", event.getEspnId(), ex);
            }
        }

        log.info("Finished match/event sync for gameweek {}. Persisted matches: {}", gameweek, persistedMatches.size());
    }

    private List<Match> upsertFootballDataMatches(List<MatchDto> matches, Season season) {
        List<Match> result = new ArrayList<>();

        for (MatchDto dto : matches) {
            if (dto.getId() == null || dto.getHomeTeam() == null || dto.getAwayTeam() == null) {
                log.warn("Skipping invalid match DTO without required fields: {}", dto);
                continue;
            }

            Optional<Team> homeTeamOpt = teamRepository.findByExternalId(dto.getHomeTeam().getId());
            Optional<Team> awayTeamOpt = teamRepository.findByExternalId(dto.getAwayTeam().getId());
            if (homeTeamOpt.isEmpty() || awayTeamOpt.isEmpty()) {
                log.warn("Skipping match {} because home/away team is missing in database", dto.getId());
                continue;
            }

            Team homeTeam = homeTeamOpt.get();
            Team awayTeam = awayTeamOpt.get();

            Match match = matchRepository.findByExternalId(dto.getId()).orElse(null);
            if (match == null) {
                match = footballDataMatchEntityMapper.toEntity(dto, homeTeam, awayTeam, season);
            } else {
                footballDataMatchEntityMapper.updateEntity(dto, homeTeam, awayTeam, season, match);
            }

            result.add(matchRepository.save(match));
        }

        return result;
    }

    private Map<String, Team> loadTeamsByEspnId() {
        Map<String, Team> teamsByEspnId = new HashMap<>();
        for (Team team : teamRepository.findAll()) {
            if (team.getEspnId() != null && !team.getEspnId().isBlank()) {
                teamsByEspnId.put(team.getEspnId(), team);
            }
        }
        return teamsByEspnId;
    }

    private Map<String, Player> loadPlayersByEspnId(List<EspnEvent> events) {
        Set<String> athleteIds = new HashSet<>();

        for (EspnEvent event : events) {
            EspnCompetition competition = getFirstCompetition(event);
            if (competition == null || competition.getDetails() == null) {
                continue;
            }

            for (EspnDetail detail : competition.getDetails()) {
                if (detail.getAthletesInvolved() == null) {
                    continue;
                }
                detail.getAthletesInvolved().forEach(athlete -> {
                    if (athlete != null && athlete.getEspnId() != null && !athlete.getEspnId().isBlank()) {
                        athleteIds.add(athlete.getEspnId());
                    }
                });
            }
        }

        Map<String, Player> playersByEspnId = new HashMap<>();
        if (athleteIds.isEmpty()) {
            return playersByEspnId;
        }

        for (Player player : playerRepository.findByEspnIdIn(athleteIds)) {
            if (player.getEspnId() != null) {
                playersByEspnId.put(player.getEspnId(), player);
            }
        }

        return playersByEspnId;
    }

    private EspnCompetition getFirstCompetition(EspnEvent event) {
        if (event == null || event.getCompetitions() == null || event.getCompetitions().isEmpty()) {
            return null;
        }
        return event.getCompetitions().getFirst();
    }

    private Optional<OffsetDateTime> getFirstKickoffTime(List<MatchDto> matches) {
        return matches.stream()
                .map(MatchDto::getKickoffTime)
                .filter(Objects::nonNull)
                .min(Comparator.naturalOrder());
    }

    private Optional<OffsetDateTime> getLastKickoffTime(List<MatchDto> matches) {
        return matches.stream()
                .map(MatchDto::getKickoffTime)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder());
    }
}
