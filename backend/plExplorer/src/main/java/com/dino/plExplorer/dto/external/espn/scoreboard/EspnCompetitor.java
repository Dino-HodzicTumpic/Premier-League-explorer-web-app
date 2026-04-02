package com.dino.plExplorer.dto.external.espn.scoreboard;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EspnCompetitor {
    private String homeAway; // "home" ili "away"
    private String score; // rezultat kao string, npr. "2"
    private EspnTeam team;
    private List<EspnStatistic> statistics;
}