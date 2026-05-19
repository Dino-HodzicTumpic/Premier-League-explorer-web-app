package com.dino.plExplorer.dto.response.matches;


import com.dino.plExplorer.entity.enums.MatchStatus;
import com.dino.plExplorer.entity.enums.MatchWinner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchDetailsDto {

    private String espnMatchId;
    private Integer minute;
    private MatchStatus status;

    private Integer injuryTime;

    private LocalDateTime utcDate;
    private Integer matchday;

    private MatchWinner winner;

    private String stadium;

    private Long attendance;

    private List<MatchEventsDto> matchEvents;

    private List<MatchRefereeDto> referees;


    private TeamDetailsDto homeTeam;
    private TeamDetailsDto awayTeam;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MatchAppearanceDto {

        private String playerName;
        private Integer playerId;
        private Integer shirtNumber;
        private String position;
        private boolean isStarting;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MatchRefereeDto {
        private Long Id;

        private String name;
        private String nationality;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MatchEventsDto {
        private String type;
        private Integer minute;
        private TeamDto team;
        private List<EventPlayerDto> players;
        private String card; // "yellow or red if event is booking"
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventPlayerDto {
        private PlayerRole role;
        private String playerId;
        private String name;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamDetailsDto {
        private String espnTeamId;
        private String tla;
        private String shortName;
        private String crestUrl;
        private String homeAway;
        private String formation;
        private TeamStatsDto stats;
        private Integer score;
        private List<MatchAppearanceDto> appearances;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamStatsDto{
        private Integer cornerKicks = null;
        private Integer goalKicks = null;
        private Integer offsides = null;
        private Integer fouls = null;
        private Integer ballPossession = null;
        private Integer accuratePasses = null;
        private Integer totalPasses = null;
        private Integer saves = null;
        private Integer throwIns = null;
        private Integer shots = null;
        private Integer shotsOnGoal = null;
        private Integer shotsOffGoal = null;
        private Integer yellowCards = null;
        private Integer redCards = null;
        private Integer totalBookings = null;
    }




}
