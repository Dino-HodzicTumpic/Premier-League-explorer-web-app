package com.dino.plExplorer.dto.external.espn.scoreboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EspnAthlete {
    @JsonProperty("id")
    private String espnId;
    private String displayName;
    private String jersey;
    private String position;
}