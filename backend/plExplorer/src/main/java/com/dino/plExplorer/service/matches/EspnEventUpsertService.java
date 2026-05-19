package com.dino.plExplorer.service.matches;

import com.dino.plExplorer.dto.external.espn.scoreboard.EspnCompetition;
import com.dino.plExplorer.dto.external.espn.scoreboard.EspnDetail;
import com.dino.plExplorer.dto.external.espn.scoreboard.EspnEvent;
import com.dino.plExplorer.dto.external.espn.summary.EspnSummaryResponse;
import com.dino.plExplorer.entity.Booking;
import com.dino.plExplorer.entity.Goal;
import com.dino.plExplorer.entity.Match;
import com.dino.plExplorer.entity.MatchAppearance;
import com.dino.plExplorer.entity.MatchStatistic;
import com.dino.plExplorer.entity.Player;
import com.dino.plExplorer.entity.Substitution;
import com.dino.plExplorer.entity.Team;
import com.dino.plExplorer.mapper.matches.EspnMatchEventMapper;
import com.dino.plExplorer.repository.BookingRepository;
import com.dino.plExplorer.repository.GoalRepository;
import com.dino.plExplorer.repository.MatchAppearanceRepository;
import com.dino.plExplorer.repository.MatchRepository;
import com.dino.plExplorer.repository.MatchRefereeRepository;
import com.dino.plExplorer.repository.MatchStatisticRepository;
import com.dino.plExplorer.repository.PlayerRepository;
import com.dino.plExplorer.repository.SubstitutionRepository;
import com.dino.plExplorer.service.espn.EspnApiService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class EspnEventUpsertService {

    private final EspnApiService espnApiService;
    private final PlayerRepository playerRepository;

    private final GoalRepository goalRepository;
    private final BookingRepository bookingRepository;
    private final SubstitutionRepository substitutionRepository;
    private final MatchAppearanceRepository matchAppearanceRepository;
    private final MatchRefereeRepository matchRefereeRepository;
    private final MatchStatisticRepository matchStatisticRepository;
    private final MatchRepository matchRepository;

    private final EspnMatchEventMapper espnMatchEventMapper;

    @Transactional
    public void upsertEspnEvent(EspnEvent event,
                                List<Match> persistedMatches,
                                Map<String, Team> teamsByEspnId,
                                Map<String, Player> playersByEspnId) {

        EspnCompetition competition = getFirstCompetition(event);
        if (competition == null) {
            log.warn("Skipping ESPN event {} because competition is missing", event != null ? event.getEspnId() : null);
            return;
        }

        Team homeTeam = resolveCompetitionTeam(competition, "home", teamsByEspnId);
        Team awayTeam = resolveCompetitionTeam(competition, "away", teamsByEspnId);
        if (homeTeam == null || awayTeam == null) {
            log.warn("Skipping ESPN event {} because home/away team could not be resolved by ESPN id", event.getEspnId());
            return;
        }

        Match match = findPersistedMatchForEvent(event, persistedMatches, homeTeam, awayTeam);
        if (match == null) {
            log.warn("No local match found for ESPN event {} (home={}, away={})", event.getEspnId(), homeTeam.getName(), awayTeam.getName());
            return;
        }

        Match managedMatch = matchRepository.findById(match.getId()).orElse(null);
        if (managedMatch == null) {
            log.warn("Match {} no longer exists when processing ESPN event {}", match.getId(), event.getEspnId());
            return;
        }

       espnMatchEventMapper.updateMatchFromEspn(event, competition, managedMatch);
       matchRepository.save(managedMatch);

        // Rebuild match events to keep sync idempotent when rerunning the same gameweek sync.
        goalRepository.deleteByMatch(managedMatch);
        bookingRepository.deleteByMatch(managedMatch);
        substitutionRepository.deleteByMatch(managedMatch);
        matchAppearanceRepository.deleteByMatch(managedMatch);
        matchRefereeRepository.deleteByMatch(managedMatch);
        matchStatisticRepository.deleteByMatch(managedMatch);

        Optional<EspnSummaryResponse> summaryOpt = fetchMatchSummary(event.getEspnId());

        applyFormations(managedMatch, summaryOpt.orElse(null), homeTeam, awayTeam);
        matchRepository.save(managedMatch);

        List<MatchStatistic> statistics = mapMatchStatistics(managedMatch, summaryOpt.orElse(null), homeTeam, awayTeam, teamsByEspnId);
        if (!statistics.isEmpty()) {
            matchStatisticRepository.saveAll(statistics);
        }

        List<MatchAppearance> appearances = mapMatchAppearances(managedMatch, summaryOpt.orElse(null), teamsByEspnId, playersByEspnId);
        if (!appearances.isEmpty()) {
            matchAppearanceRepository.saveAll(appearances);
        }

        List<EspnDetail> details = competition.getDetails();
        if (details == null || details.isEmpty()) {
            return;
        }

        List<Goal> goals = new ArrayList<>();
        List<Booking> bookings = new ArrayList<>();
        List<Substitution> substitutions = new ArrayList<>();
        summaryOpt.ifPresent(summary -> warmUpPlayersFromSummary(summary, playersByEspnId));
        List<EspnSummaryResponse.Detail> summaryGoalDetails = loadSummaryGoalDetails(summaryOpt.orElse(null));

        for (EspnDetail detail : details) {
            Team detailTeam = resolveTeamByDetail(detail, teamsByEspnId);
            if (detailTeam == null) {
                continue;
            }

            if (isGoalDetail(detail)) {
                Player scorer = getAthletePlayer(detail, playersByEspnId, detailTeam);
                if (scorer == null) {
                    continue;
                }
                Player assist = resolveAssistPlayer(detail, scorer, summaryGoalDetails, playersByEspnId, teamsByEspnId);
                goals.add(espnMatchEventMapper.toGoal(detail, managedMatch, detailTeam, scorer, assist));
                continue;
            }

            if (isCardDetail(detail)) {
                Player bookedPlayer = getAthletePlayer(detail, playersByEspnId, detailTeam);
                if (bookedPlayer == null) {
                    continue;
                }
                bookings.add(espnMatchEventMapper.toBooking(detail, managedMatch, detailTeam, bookedPlayer));
            }
        }

        if (summaryOpt.isPresent() && summaryOpt.get().getRosters() != null) {
            summaryOpt.get().getRosters().forEach(roster -> addSubs(roster, managedMatch, substitutions, teamsByEspnId, playersByEspnId));
        } else {
            log.info("No summary found for event {}, skipping substitutions", event.getEspnId());
        }

        if (!goals.isEmpty()) {
            goalRepository.saveAll(goals);
        }
        if (!bookings.isEmpty()) {
            bookingRepository.saveAll(bookings);
        }
        if (!substitutions.isEmpty()) {
            substitutionRepository.saveAll(substitutions);
        }
    }

    private Match findPersistedMatchForEvent(EspnEvent event, List<Match> persistedMatches, Team homeTeam, Team awayTeam) {
        if (event == null || event.getStartDate() == null) {
            return null;
        }
        LocalDate eventDate = event.getStartDate().toLocalDate();

        return persistedMatches.stream()
                .filter(m -> Objects.equals(m.getHomeTeam().getId(), homeTeam.getId()))
                .filter(m -> Objects.equals(m.getAwayTeam().getId(), awayTeam.getId()))
                .filter(m -> m.getUtcDate() != null && m.getUtcDate().toLocalDate().equals(eventDate))
                .findFirst()
                .orElse(null);
    }

    private EspnCompetition getFirstCompetition(EspnEvent event) {
        if (event == null || event.getCompetitions() == null || event.getCompetitions().isEmpty()) {
            return null;
        }
        return event.getCompetitions().getFirst();
    }

    private Team resolveCompetitionTeam(EspnCompetition competition, String homeAway, Map<String, Team> teamsByEspnId) {
        if (competition.getCompetitors() == null) {
            return null;
        }

        return competition.getCompetitors().stream()
                .filter(c -> homeAway.equalsIgnoreCase(c.getHomeAway()))
                .filter(c -> c.getTeam() != null && c.getTeam().getId() != null)
                .map(c -> teamsByEspnId.get(c.getTeam().getId()))
                .findFirst()
                .orElse(null);
    }

    private Team resolveTeamByDetail(EspnDetail detail, Map<String, Team> teamsByEspnId) {
        if (detail == null || detail.getTeam() == null || detail.getTeam().getEspnId() == null) {
            return null;
        }
        return teamsByEspnId.get(detail.getTeam().getEspnId());
    }

    private Player getAthletePlayer(EspnDetail detail,
                                    Map<String, Player> playersByEspnId,
                                    Team team) {
        if (detail == null || detail.getAthletesInvolved() == null || detail.getAthletesInvolved().isEmpty()) {
            return null;
        }
        if (detail.getAthletesInvolved().getFirst() == null) {
            return null;
        }

        String espnId = detail.getAthletesInvolved().getFirst().getEspnId();
        String fullName = detail.getAthletesInvolved().getFirst().getDisplayName();
        return resolveOrCreatePlayer(espnId, fullName, team, playersByEspnId);
    }

    private boolean isGoalDetail(EspnDetail detail) {
        return detail != null && Boolean.TRUE.equals(detail.getScoringPlay());
    }

    private boolean isCardDetail(EspnDetail detail) {
        return detail != null && (Boolean.TRUE.equals(detail.getYellowCard()) || Boolean.TRUE.equals(detail.getRedCard()));
    }

    private List<EspnSummaryResponse.Detail> loadSummaryGoalDetails(EspnSummaryResponse summary) {
        if (summary == null || summary.getHeader() == null || summary.getHeader().getCompetitions() == null) {
            return Collections.emptyList();
        }

        List<EspnSummaryResponse.Competition> competitions = summary.getHeader().getCompetitions();
        if (competitions.isEmpty() || competitions.getFirst() == null || competitions.getFirst().getDetails() == null) {
            return Collections.emptyList();
        }

        return competitions.getFirst().getDetails();
    }

    private Optional<EspnSummaryResponse> fetchMatchSummary(String eventEspnId) {
        return espnApiService.fetchEventSummary(eventEspnId);
    }

    private void warmUpPlayersFromSummary(EspnSummaryResponse summary, Map<String, Player> playersByEspnId) {
        if (summary == null || summary.getRosters() == null || summary.getRosters().isEmpty()) {
            return;
        }

        Set<String> missingIds = summary.getRosters().stream()
                .filter(Objects::nonNull)
                .map(EspnSummaryResponse.RosterData::getRoster)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .flatMap(member -> {
                    List<String> ids = new ArrayList<>(2);
                    if (member.getAthlete() != null && member.getAthlete().getEspnId() != null) {
                        ids.add(member.getAthlete().getEspnId());
                    }
                    if (member.getSubbedOutFor() != null
                            && member.getSubbedOutFor().getAthlete() != null
                            && member.getSubbedOutFor().getAthlete().getEspnId() != null) {
                        ids.add(member.getSubbedOutFor().getAthlete().getEspnId());
                    }
                    return ids.stream();
                })
                .filter(id -> !id.isBlank())
                .filter(id -> !playersByEspnId.containsKey(id))
                .collect(Collectors.toSet());

        if (missingIds.isEmpty()) {
            return;
        }

        for (Player player : playerRepository.findByEspnIdIn(missingIds)) {
            if (player.getEspnId() != null) {
                playersByEspnId.put(player.getEspnId(), player);
            }
        }
    }

    private Player resolveAssistPlayer(EspnDetail detail,
                                       Player scorer,
                                       List<EspnSummaryResponse.Detail> summaryGoalDetails,
                                       Map<String, Player> playersByEspnId,
                                       Map<String, Team> teamsByEspnId) {
        Integer minute = espnMatchEventMapper.parseMinute(detail);
        String scorerEspnId = scorer.getEspnId();

        return summaryGoalDetails.stream()
                .filter(EspnSummaryResponse.Detail::getScoringPlay)
                .filter(summaryDetail -> Objects.equals(parseMinute(summaryDetail), minute))
                .filter(summaryDetail -> Objects.equals(getParticipantEspnId(summaryDetail, 0), scorerEspnId))
                .map(summaryDetail -> resolveOrCreatePlayer(
                        getParticipantEspnId(summaryDetail, 1),
                        getParticipantFullName(summaryDetail, 1),
                        resolveSummaryDetailTeam(summaryDetail, teamsByEspnId),
                        playersByEspnId))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private String getParticipantFullName(EspnSummaryResponse.Detail detail, int index) {
        if (detail == null || detail.getParticipants() == null || detail.getParticipants().size() <= index) {
            return null;
        }
        EspnSummaryResponse.Participant participant = detail.getParticipants().get(index);
        if (participant == null || participant.getAthlete() == null) {
            return null;
        }
        return participant.getAthlete().getFullName();
    }

    private Team resolveSummaryDetailTeam(EspnSummaryResponse.Detail detail, Map<String, Team> teamsByEspnId) {
        if (detail == null || detail.getTeam() == null || detail.getTeam().getEspnId() == null) {
            return null;
        }
        return teamsByEspnId.get(detail.getTeam().getEspnId());
    }

    private String getParticipantEspnId(EspnSummaryResponse.Detail detail, int index) {
        if (detail == null || detail.getParticipants() == null || detail.getParticipants().size() <= index) {
            return null;
        }
        EspnSummaryResponse.Participant participant = detail.getParticipants().get(index);
        if (participant == null || participant.getAthlete() == null) {
            return null;
        }
        return participant.getAthlete().getEspnId();
    }

    private Integer parseMinute(EspnSummaryResponse.Detail detail) {
        if (detail == null || detail.getClock() == null || detail.getClock().getMinute() == null) {
            return null;
        }
        String minute = detail.getClock().getMinute();
        String[] plusSplit = minute.split("\\+");
        String base = plusSplit[0].replace("'", "").trim();
        try {
            return Integer.parseInt(base);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private Integer parseMinute(String minuteStr){
        if (minuteStr == null) {
            return null;
        }

        String[] plusSplit = minuteStr.split("\\+");
        String base = plusSplit[0].replace("'", "").trim();
        try {
            return Integer.parseInt(base);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private void addSubs(EspnSummaryResponse.RosterData rosterData,
                         Match match,
                         List<Substitution> substitutions,
                         Map<String, Team> teamsByEspnId,
                         Map<String, Player> playersByEspnId) {

        if (rosterData == null || rosterData.getTeam() == null || rosterData.getTeam().getEspnId() == null) {
            return;
        }

        Team team = teamsByEspnId.get(rosterData.getTeam().getEspnId());
        if (team == null) {
            log.warn("Team not found for espnId={}", rosterData.getTeam().getEspnId());
            return;
        }

        List<EspnSummaryResponse.RosterMember> substitutedPlayers = getSubstitutedPlayers(rosterData);

        substitutedPlayers.forEach(subedPlayer -> {
            String outId = subedPlayer.getAthlete() != null ? subedPlayer.getAthlete().getEspnId() : null;
            String outName = subedPlayer.getAthlete() != null ? subedPlayer.getAthlete().getFullName() : null;
            String inId = subedPlayer.getSubbedOutFor() != null && subedPlayer.getSubbedOutFor().getAthlete() != null
                    ? subedPlayer.getSubbedOutFor().getAthlete().getEspnId()
                    : null;
            String inName = subedPlayer.getSubbedOutFor() != null && subedPlayer.getSubbedOutFor().getAthlete() != null
                    ? subedPlayer.getSubbedOutFor().getAthlete().getFullName()
                    : null;

            Player outPlayer = resolveOrCreatePlayer(outId, outName, team, playersByEspnId);
            Player inPlayer = resolveOrCreatePlayer(inId, inName, team, playersByEspnId);
            if (inPlayer == null || outPlayer == null) {
                return;
            }

            String minute = Optional.ofNullable(subedPlayer.getPlays())
                    .orElse(List.of())
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(play -> Boolean.TRUE.equals(play.getSubstitution()))
                    .findFirst()
                    .map(EspnSummaryResponse.Play::getClock)
                    .filter(Objects::nonNull)
                    .map(EspnSummaryResponse.Clock::getMinute)
                    .orElse(null);
            Integer parsedMinute = parseMinute(minute);

            substitutions.add(espnMatchEventMapper.toSubstitution(parsedMinute, match, team, inPlayer, outPlayer));
        });
    }

    private List<EspnSummaryResponse.RosterMember> getSubstitutedPlayers(EspnSummaryResponse.RosterData rosterData) {
        if (rosterData.getRoster() == null || rosterData.getRoster().isEmpty()) {
            log.info("No roster information, skipping substitutions");
            return List.of();
        }

        return rosterData.getRoster().stream()
                .filter(Objects::nonNull)
                .filter(member -> Boolean.TRUE.equals(member.getSubbedOut()))
                .toList();
    }

    private Player resolveOrCreatePlayer(String espnId,
                                         String fullName,
                                         Team team,
                                         Map<String, Player> playersByEspnId) {
        if (espnId == null || espnId.isBlank()) {
            return null;
        }

        Player existing = playersByEspnId.get(espnId);
        if (existing != null) {
            return existing;
        }

        Optional<Player> dbPlayer = playerRepository.findByEspnId(espnId);
        if (dbPlayer.isPresent()) {
            Player player = dbPlayer.get();
            playersByEspnId.put(espnId, player);
            return player;
        }

        Player created = playerRepository.save(Player.builder()
                .espnId(espnId)
                .name(resolvePlayerName(fullName, espnId))
                .currentTeam(team)
                .build());

        playersByEspnId.put(espnId, created);
        log.info("Created missing player from ESPN feed: espnId={}, name={}", espnId, created.getName());
        return created;
    }

    private String resolvePlayerName(String fullName, String espnId) {
        if (fullName != null && !fullName.isBlank()) {
            return fullName;
        }
        return "Unknown ESPN " + espnId;
    }

    private List<MatchStatistic> mapMatchStatistics(Match match,
                                                    EspnSummaryResponse summary,
                                                    Team homeTeam,
                                                    Team awayTeam,
                                                    Map<String, Team> teamsByEspnId) {
        if (summary == null || summary.getBoxscore() == null || summary.getBoxscore().getTeams() == null || summary.getBoxscore().getTeams().isEmpty()) {
            return List.of();
        }

        List<MatchStatistic> result = new ArrayList<>();
        for (EspnSummaryResponse.TeamStats teamStats : summary.getBoxscore().getTeams()) {
            Team team = resolveStatisticTeam(teamStats, homeTeam, awayTeam, teamsByEspnId);
            if (team == null) {
                continue;
            }
            result.add(espnMatchEventMapper.toMatchStatistic(match, team, teamStats.getStatistics()));
        }

        return result;
    }

    private Team resolveStatisticTeam(EspnSummaryResponse.TeamStats teamStats,
                                      Team homeTeam,
                                      Team awayTeam,
                                      Map<String, Team> teamsByEspnId) {
        if (teamStats == null) {
            return null;
        }

        if (teamStats.getTeam() != null && teamStats.getTeam().getEspnId() != null) {
            Team fromMap = teamsByEspnId.get(teamStats.getTeam().getEspnId());
            if (fromMap != null) {
                return fromMap;
            }
        }

        if ("home".equalsIgnoreCase(teamStats.getHomeAway())) {
            return homeTeam;
        }
        if ("away".equalsIgnoreCase(teamStats.getHomeAway())) {
            return awayTeam;
        }
        return null;
    }

    private List<MatchAppearance> mapMatchAppearances(Match match,
                                                      EspnSummaryResponse summary,
                                                      Map<String, Team> teamsByEspnId,
                                                      Map<String, Player> playersByEspnId) {
        if (summary == null || summary.getRosters() == null || summary.getRosters().isEmpty()) {
            return List.of();
        }

        List<MatchAppearance> result = new ArrayList<>();
        for (EspnSummaryResponse.RosterData rosterData : summary.getRosters()) {
            if (rosterData == null || rosterData.getTeam() == null || rosterData.getTeam().getEspnId() == null) {
                continue;
            }

            Team team = teamsByEspnId.get(rosterData.getTeam().getEspnId());
            if (team == null) {
                continue;
            }

            List<EspnSummaryResponse.RosterMember> roster = Optional.ofNullable(rosterData.getRoster()).orElse(List.of());
            for (EspnSummaryResponse.RosterMember member : roster) {
                if (member == null || member.getAthlete() == null || member.getAthlete().getEspnId() == null) {
                    continue;
                }

                Player player = resolveOrCreatePlayer(
                        member.getAthlete().getEspnId(),
                        member.getAthlete().getFullName(),
                        team,
                        playersByEspnId);
                if (player == null) {
                    continue;
                }

                result.add(MatchAppearance.builder()
                        .match(match)
                        .team(team)
                        .player(player)
                        .shirtNumber(parseJerseyNumber(member.getJersey()))
                        .position(resolvePositionName(member.getPosition()))
                        .isStarting(Boolean.TRUE.equals(member.getIsStarter()))
                        .build());
            }
        }

        return result;
    }

    private Integer parseJerseyNumber(String jersey) {
        if (jersey == null || jersey.isBlank()) {
            return 0;
        }
        try {
            return Integer.parseInt(jersey.trim());
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    private String resolvePositionName(EspnSummaryResponse.Position position) {
        if (position == null || position.getName() == null || position.getName().isBlank()) {
            return "Unknown";
        }
        return position.getName();
    }

    private void applyFormations(Match match,
                                 EspnSummaryResponse summary,
                                 Team homeTeam,
                                 Team awayTeam) {
        if (match == null || summary == null || summary.getRosters() == null || summary.getRosters().isEmpty()) {
            return;
        }

        String homeFormation = null;
        String awayFormation = null;

        for (EspnSummaryResponse.RosterData rosterData : summary.getRosters()) {
            if (rosterData == null || rosterData.getFormation() == null || rosterData.getFormation().isBlank()) {
                continue;
            }

            String homeAway = rosterData.getHomeAway();
            if (homeAway != null) {
                if ("home".equalsIgnoreCase(homeAway)) {
                    homeFormation = rosterData.getFormation();
                } else if ("away".equalsIgnoreCase(homeAway)) {
                    awayFormation = rosterData.getFormation();
                }
            }

            if (rosterData.getTeam() != null && rosterData.getTeam().getEspnId() != null) {
                String rosterTeamId = rosterData.getTeam().getEspnId();
                if (homeTeam != null && rosterTeamId.equals(homeTeam.getEspnId())) {
                    homeFormation = rosterData.getFormation();
                } else if (awayTeam != null && rosterTeamId.equals(awayTeam.getEspnId())) {
                    awayFormation = rosterData.getFormation();
                }
            }
        }

        if (homeFormation != null) {
            match.setHomeFormation(homeFormation);
        }
        if (awayFormation != null) {
            match.setAwayFormation(awayFormation);
        }
    }
}




