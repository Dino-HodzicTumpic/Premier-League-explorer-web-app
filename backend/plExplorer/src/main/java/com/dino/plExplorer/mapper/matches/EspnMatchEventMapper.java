package com.dino.plExplorer.mapper.matches;

import com.dino.plExplorer.config.mapper.MapStructConfig;
import com.dino.plExplorer.dto.external.espn.scoreboard.EspnCompetition;
import com.dino.plExplorer.dto.external.espn.scoreboard.EspnStatistic;
import com.dino.plExplorer.dto.external.espn.scoreboard.EspnDetail;
import com.dino.plExplorer.dto.external.espn.scoreboard.EspnEvent;
import com.dino.plExplorer.entity.Booking;
import com.dino.plExplorer.entity.Goal;
import com.dino.plExplorer.entity.Match;
import com.dino.plExplorer.entity.MatchStatistic;
import com.dino.plExplorer.entity.Player;
import com.dino.plExplorer.entity.Substitution;
import com.dino.plExplorer.entity.Team;
import com.dino.plExplorer.entity.enums.CardType;
import com.dino.plExplorer.entity.enums.GoalType;
import com.dino.plExplorer.entity.enums.MatchStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Mapper(config = MapStructConfig.class)
public interface EspnMatchEventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    @Mapping(target = "season", ignore = true)
    @Mapping(target = "homeTeam", ignore = true)
    @Mapping(target = "awayTeam", ignore = true)
    @Mapping(target = "matchday", ignore = true)
    @Mapping(target = "homeWinOdds", ignore = true)
    @Mapping(target = "drawOdds", ignore = true)
    @Mapping(target = "awayWinOdds", ignore = true)
    @Mapping(target = "homeScoreHalfTime", ignore = true)
    @Mapping(target = "awayScoreHalfTime", ignore = true)
    @Mapping(target = "winner", ignore = true)
    @Mapping(target = "goals", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "statistics", ignore = true)
    @Mapping(target = "appearances", ignore = true)
    @Mapping(target = "substitutions", ignore = true)
    @Mapping(target = "referees", ignore = true)
    @Mapping(target = "espnId", source = "event.espnId")
    @Mapping(target = "utcDate", expression = "java(toUtcDate(event.getStartDate(), competition.getStartDate(), match.getUtcDate()))")
    @Mapping(target = "status", expression = "java(toStatus(competition))")
    @Mapping(target = "stadium", source = "competition.venue.fullName")
    @Mapping(target = "attendance", source = "competition.attendance")
    @Mapping(target = "homeScore", expression = "java(readHomeScore(competition, match.getHomeScore()))")
    @Mapping(target = "awayScore", expression = "java(readAwayScore(competition, match.getAwayScore()))")
    @Mapping(target = "injuryTime", expression = "java(parseInjuryTimeFromStatus(competition))")

    void updateMatchFromEspn(EspnEvent event, EspnCompetition competition, @MappingTarget Match match);

    default Goal toGoal(EspnDetail detail, Match match, Team team, Player scorer, Player assist) {
        return Goal.builder()
                .match(match)
                .team(team)
                .scorer(scorer)
                .assist(assist)
                .minute(parseMinute(detail))
                .injuryTime(parseInjuryTime(detail))
                .type(toGoalType(detail))
                .build();
    }

    default Goal toGoal(EspnDetail detail, Match match, Team team, Player scorer) {
        return toGoal(detail, match, team, scorer, null);
    }

    default Booking toBooking(EspnDetail detail, Match match, Team team, Player player) {
        return Booking.builder()
                .match(match)
                .team(team)
                .player(player)
                .minute(parseMinute(detail))
                .card(toCardType(detail))
                .build();
    }

    default Substitution toSubstitution(Integer minute, Match match, Team team, Player playerOut, Player playerIn) {
        return Substitution.builder()
                .match(match)
                .team(team)
                .playerOut(playerOut)
                .playerIn(playerIn)
                .minute(minute)
                .injuryTime(null)
                .build();
    }

    default MatchStatistic toMatchStatistic(Match match, Team team, List<EspnStatistic> rawStatistics) {
        Map<String, Integer> stats = toNormalizedStatMap(rawStatistics);
        Integer totalShots =  readStat(stats, 0, "totalShots");
        Integer shotsOnTarget =  readStat(stats, 0, "shotsOnTarget");
        Integer yellowCards = readStat(stats, 0, "yellowCards");
        Integer redCards = readStat(stats, 0, "redCards");

        return MatchStatistic.builder()
                .match(match)
                .team(team)
                .cornerKicks(readStat(stats, 0, "wonCorners"))
                .fouls(readStat(stats, 0, "foulsCommitted"))
                .ballPossession(readStat(stats, 0, "possessionPct"))
                .shots(totalShots)
                .shotsOnGoal(shotsOnTarget)
                .shotsOffGoal(totalShots -  shotsOnTarget)
                .saves(readStat(stats, 0, "saves"))
                .redCards(redCards)
                .yellowCards(yellowCards)
                .totalBookings(redCards + yellowCards)
                .offsides(readStat(stats, 0, "offsides"))
                .accuratePasses(readStat(stats, 0, "accuratePasses"))
                .totalPasses(readStat(stats, 0, "totalPasses"))
                .build();
    }

    default MatchStatus toStatus(EspnCompetition competition) {
        if (competition == null || competition.getStatus() == null || competition.getStatus().getType() == null) {
            return MatchStatus.SCHEDULED;
        }
        String name = competition.getStatus().getType().getName();
        if (name == null) {
            return MatchStatus.SCHEDULED;
        }
        return switch (name) {
            case "STATUS_FULL_TIME" -> MatchStatus.FINISHED;
            case "STATUS_IN_PROGRESS" -> MatchStatus.LIVE;
            case "STATUS_HALFTIME" -> MatchStatus.PAUSED;
            case "STATUS_POSTPONED" -> MatchStatus.POSTPONED;
            case "STATUS_SUSPENDED" -> MatchStatus.SUSPENDED;
            case "STATUS_CANCELED" -> MatchStatus.CANCELLED;
            default -> MatchStatus.SCHEDULED;
        };
    }

    default GoalType toGoalType(EspnDetail detail) {
        if (detail != null && Boolean.TRUE.equals(detail.getOwnGoal())) {
            return GoalType.OWN_GOAL;
        }
        if (detail != null && Boolean.TRUE.equals(detail.getPenaltyKick())) {
            return GoalType.PENALTY;
        }
        return GoalType.REGULAR;
    }

    default CardType toCardType(EspnDetail detail) {
        return detail != null && Boolean.TRUE.equals(detail.getRedCard()) ? CardType.RED : CardType.YELLOW;
    }

    default Integer readHomeScore(EspnCompetition competition, Integer fallback) {
        return competition == null || competition.getCompetitors() == null
                ? fallback
                : competition.getCompetitors().stream()
                .filter(c -> "home".equalsIgnoreCase(c.getHomeAway()))
                .findFirst()
                .map(c -> parseInteger(c.getScore(), fallback))
                .orElse(fallback);
    }

    default Integer readAwayScore(EspnCompetition competition, Integer fallback) {
        return competition == null || competition.getCompetitors() == null
                ? fallback
                : competition.getCompetitors().stream()
                .filter(c -> "away".equalsIgnoreCase(c.getHomeAway()))
                .findFirst()
                .map(c -> parseInteger(c.getScore(), fallback))
                .orElse(fallback);
    }

    default Integer parseMinute(EspnDetail detail) {
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

    default Integer parseInjuryTime(EspnDetail detail) {
        if (detail == null || detail.getClock() == null || detail.getClock().getMinute() == null) {
            return null;
        }
        String minute = detail.getClock().getMinute();
        if (!minute.contains("+")) {
            return null;
        }
        String plusPart = minute.substring(minute.indexOf('+') + 1).replace("'", "").trim();
        try {
            return Integer.parseInt(plusPart);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    default Integer parseInjuryTimeFromStatus(EspnCompetition competition) {
        if (competition == null || competition.getStatus() == null || competition.getStatus().getType() == null) {
            return null;
        }
        String detail = competition.getStatus().getType().getDetail();
        if (detail == null || !detail.contains("+")) {
            return null;
        }
        String plusPart = detail.substring(detail.indexOf('+') + 1).replace("'", "").trim();
        try {
            return Integer.parseInt(plusPart);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    default Integer parseInteger(String raw, Integer fallback) {
        if (raw == null || raw.isBlank()) {
            return fallback;
        }
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }

    default Map<String, Integer> toNormalizedStatMap(List<EspnStatistic> rawStatistics) {
        Map<String, Integer> result = new HashMap<>();
        if (rawStatistics == null) {
            return result;
        }

        for (EspnStatistic statistic : rawStatistics) {
            if (statistic == null || statistic.getName() == null || statistic.getDisplayValue() == null) {
                continue;
            }

            Integer value = parseStatNumber(statistic.getDisplayValue());
            if (value == null) {
                continue;
            }

                result.put(statistic.getName(), value);

        }

        return result;
    }

    default Integer readStat(Map<String, Integer> stats, Integer fallback, String... keys) {
        if (stats == null || keys == null) {
            return fallback;
        }
        for (String key : keys) {
            Integer value = stats.get(key);
            if (value != null) {
                return value;
            }
        }
        return fallback;
    }



    default Integer parseStatNumber(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }

        try {
            if (raw.contains(".")) {
                return (int) Math.round(Double.parseDouble(raw));
            }
            return Integer.parseInt(raw);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    default java.time.LocalDateTime toUtcDate(OffsetDateTime eventStart, String competitionStart, java.time.LocalDateTime fallback) {
        if (eventStart != null) {
            return eventStart.toLocalDateTime();
        }
        if (competitionStart != null) {
            try {
                return OffsetDateTime.parse(competitionStart).toLocalDateTime();
            } catch (Exception ignored) {
                return fallback;
            }
        }
        return fallback;
    }
}

