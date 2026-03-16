package com.dino.plExplorer.dto.external.footballdata.topScorers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class Scorer {
    private Player player;
    private Team team;
    private Integer goals;
    private Integer assists;
    private Integer penalties;
}
