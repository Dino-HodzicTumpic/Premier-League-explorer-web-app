package com.dino.plExplorer.dto.response.matches;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchDto {

    private Long id;
    private String status;
    private Integer matchday;

    private TeamDto homeTeam;
    private TeamDto awayTeam;

    private OffsetDateTime kickoffTime;

    private ScoreDto score;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamDto {
        private Long id;
        private String name;
        private String shortName;
        private String tla;
        private String crestUrl;
    }

    public enum MatchResult {
        HOME_TEAM, AWAY_TEAM, DRAW
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScoreDto {
        private MatchResult winner;
        private FullTimeDto fullTime;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FullTimeDto {
        private Integer home;
        private Integer away;
    }
}
