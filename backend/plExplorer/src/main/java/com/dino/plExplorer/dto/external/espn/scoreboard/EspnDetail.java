package com.dino.plExplorer.dto.external.espn.scoreboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// EspnDetail.java - gol/žuti/crveni karton eventi
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EspnDetail {
    private EspnDetailType type;
    private EspnClock clock;
    private EspnDetailTeam team;
    private Boolean scoringPlay;
    private Boolean redCard;
    private Boolean yellowCard;
    private Boolean ownGoal;
    private Boolean penaltyKick;
    private List<EspnAthlete> athletesInvolved;

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EspnDetailType {
        private String text; // "Goal", "Yellow Card", "Red Card"
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EspnClock {
        @JsonProperty("displayValue")
        private String minute; // "10'", "44'"
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EspnDetailTeam {
        @JsonProperty("id")
        private String espnId;
    }
}
