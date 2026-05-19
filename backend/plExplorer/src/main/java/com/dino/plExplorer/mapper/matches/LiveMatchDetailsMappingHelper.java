package com.dino.plExplorer.mapper.matches;

import com.dino.plExplorer.dto.response.matches.LiveMatchDetailsSnapshotDto;
import com.dino.plExplorer.dto.response.matches.MatchDetailsDto;
import com.dino.plExplorer.dto.response.matches.TeamDto;
import com.dino.plExplorer.entity.enums.MatchStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LiveMatchDetailsMappingHelper {

    private LiveMatchDetailsMappingHelper() {
    }

    public static MatchStatus mapStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        try {
            return MatchStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    public static MatchDetailsDto.TeamDetailsDto toTeamDetailsDto(LiveMatchDetailsSnapshotDto.TeamDetailsDto team) {
        if (team == null) {
            return null;
        }

        return MatchDetailsDto.TeamDetailsDto.builder()
                .espnTeamId(team.getEspnTeamId())
                .tla(team.getTla())
                .shortName(team.getShortName())
                .crestUrl(team.getCrestUrl())
                .homeAway(team.getHomeAway())
                .formation(team.getFormation())
                .stats(toTeamStatsDto(team.getStatistics()))
                .score(null)
                .appearances(toMatchAppearanceDtoList(team.getRoster()))
                .build();
    }

    public static MatchDetailsDto.TeamStatsDto toTeamStatsDto(List<LiveMatchDetailsSnapshotDto.StatisticDto> statistics) {
        Map<String, Integer> stats = toNormalizedStatMap(statistics);

        Integer totalShots = readStat(stats, null, "totalShots");
        Integer shotsOnTarget = readStat(stats, null, "shotsOnTarget");
        Integer yellowCards = readStat(stats, null, "yellowCards");
        Integer redCards = readStat(stats, null, "redCards");

        Integer shotsOffGoal = null;
        if (totalShots != null && shotsOnTarget != null) {
            shotsOffGoal = Math.max(totalShots - shotsOnTarget, 0);
        }

        Integer totalBookings = null;
        if (redCards != null && yellowCards != null) {
            totalBookings = redCards + yellowCards;
        }

        return MatchDetailsDto.TeamStatsDto.builder()
                .cornerKicks(readStat(stats, null, "wonCorners", "cornerKicks"))
                .goalKicks(readStat(stats, null, "goalKicks"))
                .offsides(readStat(stats, null, "offsides"))
                .fouls(readStat(stats, null, "foulsCommitted", "fouls"))
                .ballPossession(readStat(stats, null, "possessionPct", "ballPossession"))
                .accuratePasses(readStat(stats, null, "accuratePasses"))
                .totalPasses(readStat(stats, null, "totalPasses"))
                .saves(readStat(stats, null, "saves"))
                .throwIns(readStat(stats, null, "throwIns"))
                .shots(totalShots)
                .shotsOnGoal(shotsOnTarget)
                .shotsOffGoal(shotsOffGoal)
                .yellowCards(yellowCards)
                .redCards(redCards)
                .totalBookings(totalBookings)
                .build();
    }

    public static MatchDetailsDto.MatchAppearanceDto toMatchAppearanceDto(LiveMatchDetailsSnapshotDto.RosterMemberDto member) {
        if (member == null) {
            return null;
        }
        return MatchDetailsDto.MatchAppearanceDto.builder()
                .playerName(member.getFullName())
                .playerId(parseInteger(member.getPlayerEspnId(), null))
                .shirtNumber(parseInteger(member.getJersey(), null))
                .position(member.getPosition())
                .isStarting(Boolean.TRUE.equals(member.getStarter()))
                .build();
    }

    public static List<MatchDetailsDto.MatchAppearanceDto> toMatchAppearanceDtoList(List<LiveMatchDetailsSnapshotDto.RosterMemberDto> roster) {
        if (roster == null || roster.isEmpty()) {
            return Collections.emptyList();
        }
        return roster.stream().map(LiveMatchDetailsMappingHelper::toMatchAppearanceDto).toList();
    }

    public static MatchDetailsDto.MatchEventsDto toMatchEventsDto(LiveMatchDetailsSnapshotDto.DetailDto detail) {
        if (detail == null) {
            return null;
        }

        return MatchDetailsDto.MatchEventsDto.builder()
                .type(resolveEventType(detail))
                .minute(parseEventMinute(detail.getMinute()))
                .team(toTeamDto(detail.getTeam()))
                .players(toEventPlayerDtoList(detail.getParticipants()))
                .card(Boolean.TRUE.equals(detail.getRedCard()) ? "RED" : null)
                .build();
    }

    public static List<MatchDetailsDto.MatchEventsDto> toMatchEventsDtoList(List<LiveMatchDetailsSnapshotDto.DetailDto> details) {
        if (details == null || details.isEmpty()) {
            return Collections.emptyList();
        }
        return details.stream().map(LiveMatchDetailsMappingHelper::toMatchEventsDto).toList();
    }

    public static MatchDetailsDto.EventPlayerDto toEventPlayerDto(LiveMatchDetailsSnapshotDto.ParticipantDto participant) {
        if (participant == null) {
            return null;
        }
        return MatchDetailsDto.EventPlayerDto.builder()
                .role(null)
                .playerId(participant.getPlayerEspnId())
                .name(participant.getFullName())
                .build();
    }

    public static List<MatchDetailsDto.EventPlayerDto> toEventPlayerDtoList(List<LiveMatchDetailsSnapshotDto.ParticipantDto> participants) {
        if (participants == null || participants.isEmpty()) {
            return Collections.emptyList();
        }
        return participants.stream().map(LiveMatchDetailsMappingHelper::toEventPlayerDto).toList();
    }

    public static TeamDto toTeamDto(LiveMatchDetailsSnapshotDto.DetailTeamDto team) {
        if (team == null) {
            return null;
        }
        return TeamDto.builder()
                .id(parseLong(team.getEspnTeamId(), null))
                .tla(team.getTla())
                .shortName(team.getShortName())
                .build();
    }

    public static String resolveEventType(LiveMatchDetailsSnapshotDto.DetailDto detail) {
        if (detail == null) {
            return null;
        }
        if (Boolean.TRUE.equals(detail.getScoringPlay())) {
            if (Boolean.TRUE.equals(detail.getOwnGoal())) {
                return "OWN_GOAL";
            }
            if (Boolean.TRUE.equals(detail.getPenaltyKick())) {
                return "PENALTY_GOAL";
            }
            return "GOAL";
        }
        if (Boolean.TRUE.equals(detail.getRedCard())) {
            return "RED_CARD";
        }
        return null;
    }

    public static Integer parseEventMinute(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        String cleaned = raw.replace("'", "").trim();
        String[] parts = cleaned.split("\\+");
        Integer base = parseInteger(parts[0], null);
        if (parts.length == 1 || base == null) {
            return base;
        }
        Integer injury = parseInteger(parts[1], null);
        if (injury == null) {
            return base;
        }
        return base + injury;
    }

    public static Integer parseInteger(String raw, Integer fallback) {
        if (raw == null || raw.isBlank()) {
            return fallback;
        }
        try {
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }

    public static Long parseLong(String raw, Long fallback) {
        if (raw == null || raw.isBlank()) {
            return fallback;
        }
        try {
            return Long.parseLong(raw.trim());
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }

    public static Map<String, Integer> toNormalizedStatMap(List<LiveMatchDetailsSnapshotDto.StatisticDto> rawStatistics) {
        Map<String, Integer> result = new HashMap<>();
        if (rawStatistics == null) {
            return result;
        }

        for (LiveMatchDetailsSnapshotDto.StatisticDto statistic : rawStatistics) {
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

    public static Integer parseStatNumber(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        String cleaned = raw.replaceAll("[^0-9.]", "");
        if (cleaned.isBlank()) {
            return null;
        }
        try {
            if (cleaned.contains(".")) {
                return (int) Math.round(Double.parseDouble(cleaned));
            }
            return Integer.parseInt(cleaned);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    public static Integer readStat(Map<String, Integer> stats, Integer fallback, String... keys) {
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
}

