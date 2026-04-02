package com.dino.plExplorer.dto.external.espn.scoreboard;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EspnEvent {
    @JsonProperty("id")
    private String espnId;
    @JsonProperty("date")
    private OffsetDateTime startDate;
    private List<EspnCompetition> competitions;
}