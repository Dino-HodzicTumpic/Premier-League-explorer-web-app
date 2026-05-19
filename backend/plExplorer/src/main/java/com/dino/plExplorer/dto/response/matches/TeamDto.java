package com.dino.plExplorer.dto.response.matches;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public  class TeamDto {
    private Long id;
    private String name;
    private String shortName;
    private String tla;
    private String crestUrl;
}