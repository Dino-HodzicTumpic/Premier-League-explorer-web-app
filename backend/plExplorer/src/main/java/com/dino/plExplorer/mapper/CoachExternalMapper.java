package com.dino.plExplorer.mapper;

import com.dino.plExplorer.config.mapper.MapStructConfig;
import com.dino.plExplorer.dto.external.footballdata.CoachExternalData;
import com.dino.plExplorer.entity.Coach;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.YearMonth;

@Mapper(config = MapStructConfig.class,
uses = DateMapper.class)
public interface CoachExternalMapper {

     @Mapping(source = "id" , target = "externalId" )
     @Mapping(source = "contract.start" , target = "contractStart", qualifiedByName = "yearMonthToStartDate")
     @Mapping(source = "contract.until" , target = "contractUntil", qualifiedByName = "yearMonthToUntilDate")

    Coach
     toCoachEntity(CoachExternalData coachExternal);


}
