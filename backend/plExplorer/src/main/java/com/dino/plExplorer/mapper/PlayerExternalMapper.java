package com.dino.plExplorer.mapper;

import com.dino.plExplorer.config.mapper.MapStructConfig;
import com.dino.plExplorer.dto.external.footballdata.initseed.PlayerExternalData;
import com.dino.plExplorer.entity.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class, uses = DateMapper.class )
public interface PlayerExternalMapper {

    @Mapping(source = "id", target = "externalId")
    @Mapping(source = "shirtNumber", target = "shirtNumber")
    @Mapping(source = "currentTeam.contract.start", target = "contractStart", qualifiedByName = "yearMonthToStartDate")
    @Mapping(source = "currentTeam.contract.until", target = "contractUntil", qualifiedByName = "yearMonthToUntilDate")
    Player toPlayerEntity(PlayerExternalData playerExternalData);
}
