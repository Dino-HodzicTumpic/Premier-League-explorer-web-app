package com.dino.plExplorer.mapper.standings;

import com.dino.plExplorer.config.mapper.MapStructConfig;
import com.dino.plExplorer.dto.external.footballdata.standings.TableEntry;
import com.dino.plExplorer.dto.response.standings.TeamStandingDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface StandingsMapper {
    @Mappings({
        @Mapping(source = "team.id", target = "externalId"),
        @Mapping(source = "team.name", target = "name"),
        @Mapping(source = "team.shortName", target = "shortName"),
        @Mapping(source = "team.tla", target = "tla"),
        @Mapping(source = "team.crest", target = "crestUrl")
    })
    TeamStandingDto toDto(TableEntry entry);

    List<TeamStandingDto> toDtoList(List<TableEntry> entries);
}

