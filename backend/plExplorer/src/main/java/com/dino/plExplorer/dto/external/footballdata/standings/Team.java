package com.dino.plExplorer.dto.external.footballdata.standings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Team {
    private Long id;
    private String name;
    private String shortName;
    private String tla;
    private String crest;
}
