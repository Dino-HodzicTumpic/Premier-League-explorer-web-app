package com.dino.plExplorer.dto.external.espn.summary;


import com.dino.plExplorer.dto.external.espn.scoreboard.EspnStatistic;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EspnSummaryResponse {

    private Header header;
    private List<RosterData> rosters;
    private BoxScore boxscore;

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BoxScore {
        private List<TeamStats> teams;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TeamStats {
        private Team team;
        private List<EspnStatistic> statistics;
        private String homeAway; // "home" ili "away"
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RosterData {
        private String homeAway; // "home" ili "away"
        private Team team;
        private List<RosterMember> roster;
        private String formation; // npr. "4-1-4-1"

    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RosterMember {
        @JsonProperty("starter")
        private Boolean isStarter;
        private String jersey; // npr "32"
        private Athlete athlete;
        private Boolean subbedIn;
        private Boolean subbedOut;
        private Position position;
        private Substitution subbedOutFor;
        private List<Play> plays;

    }



    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Substitution {
        private Athlete athlete;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Play {
        private Clock clock;
        private Boolean substitution;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Position {
        private String name; // Center right defender, Left back
    }


    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Header {
        private List<Competition> competitions;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Competition {
        private List<Detail> details;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Detail {
        private Boolean scoringPlay;
        private List<Participant> participants;
        private Team team;
        private Clock clock;
        // added afterward
        private Boolean redCard;
        private Boolean ownGoal;
        private Boolean penaltyKick;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Clock {
        @JsonProperty("displayValue")
        private String minute; // "10'", "44'"
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Team {
        @JsonProperty("id")
        private  String espnId;
        @JsonProperty("abbreviation")
        private String tla;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Participant {
        private Athlete athlete;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Athlete {
        @JsonProperty("id")
        private String espnId;
        @JsonAlias({"fullName", "displayName"})
        private String fullName;

    }

}
