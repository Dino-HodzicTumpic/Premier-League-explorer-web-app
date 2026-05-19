package com.dino.plExplorer.mapper.matches;

import com.dino.plExplorer.config.mapper.MapStructConfig;
import com.dino.plExplorer.dto.response.matches.MatchDetailsDto;
import com.dino.plExplorer.dto.response.matches.PlayerRole;
import com.dino.plExplorer.dto.response.matches.TeamDto;
import com.dino.plExplorer.entity.*;
import com.dino.plExplorer.entity.enums.CardType;
import com.dino.plExplorer.entity.enums.GoalType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Mapper(config = MapStructConfig.class)
public interface FinishedMatchDetailsMapper {

    @Mapping(target = "espnMatchId", source = "espnId")
    @Mapping(target = "utcDate", source = "utcDate")
    @Mapping(target = "matchday", source = "matchday")
    @Mapping(target = "winner", source = "winner")
    @Mapping(target = "stadium", source = "stadium")
    @Mapping(target = "attendance", source = "attendance")
    @Mapping(target = "homeTeam", expression = "java(toTeamDetailsDto(match, match.getHomeTeam(), true))")
    @Mapping(target = "awayTeam", expression = "java(toTeamDetailsDto(match, match.getAwayTeam(), false))")
    @Mapping(target = "matchEvents", expression = "java(toMatchEvents(match))")
    @Mapping(target = "referees", expression = "java(toRefereeDtos(match.getReferees()))")
    MatchDetailsDto toDto(Match match);

    default MatchDetailsDto.TeamDetailsDto toTeamDetailsDto(Match match, Team team, boolean isHome) {
        if (match == null || team == null) {
            return null;
        }

        return MatchDetailsDto.TeamDetailsDto.builder()
                .espnTeamId(team.getEspnId())
                .tla(team.getTla())
                .shortName(team.getShortName())
                .crestUrl(team.getCrestUrl())
                .homeAway(isHome ? "home" : "away")
                .formation(null)
                .stats(toTeamStatsDto(match, team))
                .score(isHome ? match.getHomeScore() : match.getAwayScore())
                .appearances(toMatchAppearanceDtos(match.getAppearances(), team))
                .build();
    }

    default MatchDetailsDto.TeamStatsDto toTeamStatsDto(Match match, Team team) {
        if (match == null || team == null || match.getStatistics() == null) {
            return null;
        }

        Optional<MatchStatistic> statOpt = match.getStatistics().stream()
                .filter(Objects::nonNull)
                .filter(stat -> team.equals(stat.getTeam()))
                .findFirst();

        if (statOpt.isEmpty()) {
            return null;
        }

        MatchStatistic stat = statOpt.get();
        return MatchDetailsDto.TeamStatsDto.builder()
                .cornerKicks(stat.getCornerKicks())
                .goalKicks(stat.getGoalKicks())
                .offsides(stat.getOffsides())
                .fouls(stat.getFouls())
                .ballPossession(stat.getBallPossession())
                .accuratePasses(stat.getAccuratePasses())
                .totalPasses(stat.getTotalPasses())
                .saves(stat.getSaves())
                .throwIns(stat.getThrowIns())
                .shots(stat.getShots())
                .shotsOnGoal(stat.getShotsOnGoal())
                .shotsOffGoal(stat.getShotsOffGoal())
                .yellowCards(stat.getYellowCards())
                .redCards(stat.getRedCards())
                .totalBookings(stat.getTotalBookings())
                .build();
    }

    default List<MatchDetailsDto.MatchAppearanceDto> toMatchAppearanceDtos(List<MatchAppearance> appearances, Team team) {
        if (appearances == null || appearances.isEmpty() || team == null) {
            return Collections.emptyList();
        }

        return appearances.stream()
                .filter(Objects::nonNull)
                .filter(app -> team.equals(app.getTeam()))
                .map(this::toMatchAppearanceDto)
                .toList();
    }

    default MatchDetailsDto.MatchAppearanceDto toMatchAppearanceDto(MatchAppearance appearance) {
        if (appearance == null) {
            return null;
        }
        Player player = appearance.getPlayer();

        return MatchDetailsDto.MatchAppearanceDto.builder()
                .playerName(player != null ? player.getName() : null)
                .playerId(toInteger(player != null ? player.getId() : null))
                .shirtNumber(appearance.getShirtNumber())
                .position(appearance.getPosition())
                .isStarting(appearance.isStarting())
                .build();
    }

    default List<MatchDetailsDto.MatchEventsDto> toMatchEvents(Match match) {
        if (match == null) {
            return Collections.emptyList();
        }

        List<MatchDetailsDto.MatchEventsDto> events = new ArrayList<>();
        if (match.getGoals() != null) {
            match.getGoals().stream()
                    .filter(Objects::nonNull)
                    .map(this::toGoalEvent)
                    .filter(Objects::nonNull)
                    .forEach(events::add);
        }

        if (match.getBookings() != null) {
            match.getBookings().stream()
                    .filter(Objects::nonNull)
                    .map(this::toBookingEvent)
                    .filter(Objects::nonNull)
                    .forEach(events::add);
        }

        if (match.getSubstitutions() != null) {
            match.getSubstitutions().stream()
                    .filter(Objects::nonNull)
                    .map(this::toSubstitutionEvent)
                    .filter(Objects::nonNull)
                    .forEach(events::add);
        }

        events.sort(Comparator.comparingInt(event -> event.getMinute() != null ? event.getMinute() : Integer.MAX_VALUE));
        return events;
    }

    default MatchDetailsDto.MatchEventsDto toGoalEvent(Goal goal) {
        if (goal == null) {
            return null;
        }

        List<MatchDetailsDto.EventPlayerDto> players = new ArrayList<>();
        if (goal.getScorer() != null) {
            players.add(MatchDetailsDto.EventPlayerDto.builder()
                    .role(PlayerRole.SCORER)
                    .playerId(toString(goal.getScorer().getId()))
                    .name(goal.getScorer().getName())
                    .build());
        }
        if (goal.getAssist() != null) {
            players.add(MatchDetailsDto.EventPlayerDto.builder()
                    .role(PlayerRole.ASSIST)
                    .playerId(toString(goal.getAssist().getId()))
                    .name(goal.getAssist().getName())
                    .build());
        }

        return MatchDetailsDto.MatchEventsDto.builder()
                .type(toGoalType(goal.getType()))
                .minute(resolveMinute(goal.getMinute(), goal.getInjuryTime()))
                .team(toTeamDto(goal.getTeam()))
                .players(players)
                .card(null)
                .build();
    }

    default MatchDetailsDto.MatchEventsDto toBookingEvent(Booking booking) {
        if (booking == null) {
            return null;
        }

        List<MatchDetailsDto.EventPlayerDto> players = new ArrayList<>();
        if (booking.getPlayer() != null) {
            players.add(MatchDetailsDto.EventPlayerDto.builder()
                    .role(PlayerRole.BOOKED)
                    .playerId(toString(booking.getPlayer().getId()))
                    .name(booking.getPlayer().getName())
                    .build());
        }

        return MatchDetailsDto.MatchEventsDto.builder()
                .type("BOOKING")
                .minute(booking.getMinute())
                .team(toTeamDto(booking.getTeam()))
                .players(players)
                .card(toCard(booking.getCard()))
                .build();
    }

    default MatchDetailsDto.MatchEventsDto toSubstitutionEvent(Substitution substitution) {
        if (substitution == null) {
            return null;
        }

        List<MatchDetailsDto.EventPlayerDto> players = new ArrayList<>();
        if (substitution.getPlayerIn() != null) {
            players.add(MatchDetailsDto.EventPlayerDto.builder()
                    .role(PlayerRole.IN)
                    .playerId(toString(substitution.getPlayerIn().getId()))
                    .name(substitution.getPlayerIn().getName())
                    .build());
        }
        if (substitution.getPlayerOut() != null) {
            players.add(MatchDetailsDto.EventPlayerDto.builder()
                    .role(PlayerRole.OUT)
                    .playerId(toString(substitution.getPlayerOut().getId()))
                    .name(substitution.getPlayerOut().getName())
                    .build());
        }

        return MatchDetailsDto.MatchEventsDto.builder()
                .type("SUBSTITUTION")
                .minute(resolveMinute(substitution.getMinute(), substitution.getInjuryTime()))
                .team(toTeamDto(substitution.getTeam()))
                .players(players)
                .card(null)
                .build();
    }

    default String toGoalType(GoalType type) {
        if (type == null) {
            return "GOAL";
        }
        return switch (type) {
            case OWN_GOAL -> "OWN_GOAL";
            case PENALTY -> "PENALTY_GOAL";
            default -> "GOAL";
        };
    }

    default String toCard(CardType card) {
        if (card == null) {
            return null;
        }
        return card.name();
    }

    default TeamDto toTeamDto(Team team) {
        if (team == null) {
            return null;
        }
        return TeamDto.builder()
                .id(team.getId())
                .name(team.getName())
                .shortName(team.getShortName())
                .tla(team.getTla())
                .crestUrl(team.getCrestUrl())
                .build();
    }

    default List<MatchDetailsDto.MatchRefereeDto> toRefereeDtos(List<MatchReferee> referees) {
        if (referees == null || referees.isEmpty()) {
            return Collections.emptyList();
        }

        return referees.stream()
                .filter(Objects::nonNull)
                .map(this::toRefereeDto)
                .toList();
    }

    default MatchDetailsDto.MatchRefereeDto toRefereeDto(MatchReferee referee) {
        if (referee == null || referee.getReferee() == null) {
            return null;
        }
        Referee ref = referee.getReferee();
        return MatchDetailsDto.MatchRefereeDto.builder()
                .Id(ref.getId())
                .name(ref.getName())
                .nationality(ref.getNationality())
                .build();
    }

    default Integer resolveMinute(Integer minute, Integer injuryTime) {
        if (minute == null) {
            return null;
        }
        if (injuryTime == null) {
            return minute;
        }
        return minute + injuryTime;
    }

    default Integer toInteger(Long value) {
        if (value == null) {
            return null;
        }
        if (value > Integer.MAX_VALUE) {
            return null;
        }
        return value.intValue();
    }

    default String toString(Long value) {
        if (value == null) {
            return null;
        }
        return String.valueOf(value);
    }
}

