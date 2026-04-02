package com.dino.plExplorer.dto.external.espn.scoreboard;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EspnStatus {
    private EspnStatusType type;

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EspnStatusType {
        private String name; // npr "STATUS_FULL_TIME" ili "STATUS_Second_Half"
        private Boolean completed; // is game finished?
        private String description; // npr "Full Time" ili "2nd Half"
        @JsonProperty("detail")
        private String detail; // minuta ili FT ako je završeno
                                // npr FT ili "90'+10'"

    }
}
