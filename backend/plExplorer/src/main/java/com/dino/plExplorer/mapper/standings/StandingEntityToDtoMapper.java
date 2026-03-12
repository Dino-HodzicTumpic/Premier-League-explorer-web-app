package com.dino.plExplorer.mapper.standings;

import com.dino.plExplorer.config.mapper.MapStructConfig;
import com.dino.plExplorer.dto.response.standings.TeamStandingDto;
import com.dino.plExplorer.entity.Standing;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface StandingEntityToDtoMapper {

    @Mappings({
            @Mapping(source = "team.externalId", target = "externalId"),
            @Mapping(source = "team.name", target = "name"),
            @Mapping(source = "team.shortName", target = "shortName"),
            @Mapping(source = "team.tla", target = "tla"),
            @Mapping(source = "team.crestUrl", target = "crestUrl"),
            @Mapping(source = "gamesPlayed", target = "playedGames")
    })
    TeamStandingDto toDto(Standing standing);

    List<TeamStandingDto> toDtoList(List<Standing> standings);
}
