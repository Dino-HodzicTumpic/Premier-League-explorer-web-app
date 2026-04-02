package com.dino.plExplorer.dto.external.footballdata.matches;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Match {
    private Long id;
    private String status;
    private Integer matchday;

    private Team homeTeam;
    private Team awayTeam;

    @JsonProperty("utcDate")
    private OffsetDateTime kickoffTime;

    private Score score;

    private List<Referee> referees;


    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Score {
        private MatchResult winner;
        private FullTime fullTime;

        @Data
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class FullTime {
            private Integer home;
            private Integer away;
        }
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Referee {
        private String name;
        private String nationality;
    }

}
