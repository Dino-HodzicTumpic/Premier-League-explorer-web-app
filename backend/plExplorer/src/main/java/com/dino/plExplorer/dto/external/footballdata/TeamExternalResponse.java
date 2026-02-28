package com.dino.plExplorer.dto.external.footballdata;

import com.dino.plExplorer.config.mapper.MapStructConfig;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamExternalResponse {

    private Long id;
    private String name;
    private String shortName;
    private String tla;
    private String crest;
    private Integer founded;
    private String clubColors;
    private String venue;
    private Long marketValue;

    private CoachExternalData coach;
    private List<SquadMemberExternalData> squad;

}
