package com.dino.plExplorer.dto.external.footballdata.matches;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Team {
    private Long id;
    private String name;
    private String shortName;
    private String tla;
    @JsonProperty("crest")
    private String crestUrl;
}
