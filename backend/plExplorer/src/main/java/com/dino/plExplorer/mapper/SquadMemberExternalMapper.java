package com.dino.plExplorer.mapper;

import com.dino.plExplorer.config.mapper.MapStructConfig;
import com.dino.plExplorer.dto.external.footballdata.SquadMemberExternalData;
import com.dino.plExplorer.entity.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class,
uses = DateMapper.class )
public interface SquadMemberExternalMapper {

    @Mapping(source = "id" , target = "externalId" )
    @Mapping(source = "contract.start" , target = "contractStart", qualifiedByName = "yearMonthToStartDate")
    @Mapping(source = "contract.until" , target = "contractUntil", qualifiedByName = "yearMonthToUntilDate")
    Player toPlayerEntity(SquadMemberExternalData squadMemberExternal);
}
