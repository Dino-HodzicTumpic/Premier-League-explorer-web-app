package com.dino.plExplorer.mapper.topScorers;

import com.dino.plExplorer.config.mapper.MapStructConfig;
import com.dino.plExplorer.dto.external.footballdata.topScorers.Scorer;
import com.dino.plExplorer.dto.external.footballdata.topScorers.TopScorersResponse;
import com.dino.plExplorer.dto.response.topScorers.TopScorersDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Collections;
import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface TopScorersMapper {

    @Mappings({
            @Mapping(source = "player.id", target = "externalId"),
            @Mapping(source = "player.name", target = "playerName"),
            @Mapping(source = "player.section", target = "playerPosition"),
            @Mapping(source = "player.nationality", target = "playerNationality"),
            @Mapping(target = "playerImageUrl", ignore = true),
            @Mapping(source = "team.name", target = "teamName"),
            @Mapping(source = "team.shortName", target = "teamShortName"),
            @Mapping(source = "team.tla", target = "teamTla"),
            @Mapping(source = "team.crest", target = "teamCrestUrl"),
            @Mapping(source = "goals", target = "numberOfGoals")
    })

    TopScorersDto toDto(Scorer scorer);

    List<TopScorersDto> toDtoList(List<Scorer> scorers);

    default List<TopScorersDto> toDtoList(TopScorersResponse response) {
        if (response == null || response.getScorers() == null) {
            return Collections.emptyList();
        }
        return toDtoList(response.getScorers());
    }
}
