package com.dino.plExplorer.dto.external.espn.scoreboard;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EspnScoreboardResponse {
    private List<EspnEvent> events;
}
