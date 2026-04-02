package com.dino.plExplorer.dto.external.espn.scoreboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EspnTeam {
    private String id;
    private String displayName;

    @JsonProperty("abbreviation")
    private String tla;
    @JsonProperty("logo")
    private String logoUrl;
}