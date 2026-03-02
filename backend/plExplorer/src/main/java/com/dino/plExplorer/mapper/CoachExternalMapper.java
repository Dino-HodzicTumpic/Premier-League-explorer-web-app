package com.dino.plExplorer.mapper;

import com.dino.plExplorer.config.mapper.MapStructConfig;
import com.dino.plExplorer.dto.external.footballdata.initseed.CoachExternalData;
import com.dino.plExplorer.entity.Coach;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class,
uses = DateMapper.class)
public interface CoachExternalMapper {

     @Mapping(source = "id" , target = "externalId" )
     @Mapping(source = "contract.start" , target = "contractStart", qualifiedByName = "yearMonthToStartDate")
     @Mapping(source = "contract.until" , target = "contractUntil", qualifiedByName = "yearMonthToUntilDate")

    Coach
     toCoachEntity(CoachExternalData coachExternal);


}
