package com.dino.plExplorer.service.matches;

import com.dino.plExplorer.dto.external.espn.scoreboard.EspnCompetition;
import com.dino.plExplorer.dto.external.espn.scoreboard.EspnDetail;
import com.dino.plExplorer.dto.external.espn.scoreboard.EspnEvent;
import com.dino.plExplorer.dto.external.espn.scoreboard.EspnScoreboardResponse;
import com.dino.plExplorer.dto.external.espn.summary.EspnSummaryResponse;
import com.dino.plExplorer.entity.Match;
import com.dino.plExplorer.entity.Player;
import com.dino.plExplorer.entity.Team;
import com.dino.plExplorer.repository.MatchRepository;
import com.dino.plExplorer.repository.PlayerRepository;
import com.dino.plExplorer.repository.TeamRepository;
import com.dino.plExplorer.service.espn.EspnApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchFinalizationService {

    private static final DateTimeFormatter ESPN_DATE_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;

    private final CacheManager cacheManager;
    private final EspnApiService espnApiService;
    private final EspnEventUpsertService espnEventUpsertService;
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    public void finalizeMatch(String espnMatchId) {
        // 1. Fresh pull direktno iz API-ja (ne iz cachea!)
        Optional<EspnSummaryResponse> summaryOpt = espnApiService.fetchEventSummary(espnMatchId);
        if (summaryOpt.isEmpty()) {
            log.error("Failed to fetch match summary for match ID {}. Match finalization aborted.", espnMatchId);
            return;
        }

        Match match = matchRepository.findByEspnId(espnMatchId).orElse(null);
        if (match == null || match.getUtcDate() == null) {
            log.warn("Match with espnId {} not found or missing utcDate; cannot finalize.", espnMatchId);
            return;
        }

        String matchDate = match.getUtcDate().toLocalDate().format(ESPN_DATE_FORMAT);
        Optional<EspnScoreboardResponse> scoreboardOpt = espnApiService.fetchScoreboardForDateRange(matchDate, matchDate);
        if (scoreboardOpt.isEmpty() || scoreboardOpt.get().getEvents() == null || scoreboardOpt.get().getEvents().isEmpty()) {
            log.warn("No ESPN scoreboard events found for match date {} (espnId={})", matchDate, espnMatchId);
            return;
        }

        EspnEvent event = scoreboardOpt.get().getEvents().stream()
                .filter(e -> espnMatchId.equals(e.getEspnId()))
                .findFirst()
                .orElse(null);
        if (event == null) {
            log.warn("ESPN event {} not found in scoreboard response for date {}", espnMatchId, matchDate);
            return;
        }

        // 2. Spremi sve u DB u jednoj transakciji (reuse ESPN upsert)
        Map<String, Team> teamsByEspnId = loadTeamsByEspnId();
        Map<String, Player> playersByEspnId = loadPlayersByEspnId(event);

        espnEventUpsertService.upsertEspnEvent(event, List.of(match), teamsByEspnId, playersByEspnId);

        // 3. Tek onda očisti oba cachea
        clearCache(espnMatchId);

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

    private Map<String, Player> loadPlayersByEspnId(EspnEvent event) {
        Set<String> athleteIds = new HashSet<>();
        EspnCompetition competition = getFirstCompetition(event);
        if (competition != null && competition.getDetails() != null) {
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

    private void clearCache(String espnMatchId) {
        Cache gameListCache = cacheManager.getCache("liveMatchListCache");
        Cache gameDetailsCache = cacheManager.getCache("liveMatchDetailsCache");
        if (gameListCache != null) {
            gameListCache.evict(espnMatchId);
            log.info("Clear live matches list cache for match ID {}.", espnMatchId);
        }

        if (gameDetailsCache != null) {
            gameDetailsCache.evict(espnMatchId);
            log.info("Clear live match details cache for match ID {}.", espnMatchId);
        }
    }
}
