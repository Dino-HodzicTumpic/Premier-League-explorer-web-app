package com.dino.plExplorer.mapper;

import com.dino.plExplorer.config.mapper.MapStructConfig;
import com.dino.plExplorer.dto.external.footballdata.initseed.TeamExternalResponse;
import com.dino.plExplorer.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class,
uses = {CoachExternalMapper.class, SquadMemberExternalMapper.class} )
public interface TeamExternalMapper {
    @Mapping(source = "id", target ="externalId" )
    @Mapping(source = "crest", target = "crestUrl")
    @Mapping(source = "founded", target = "yearFounded")
    @Mapping(source = "venue", target = "stadium")
    @Mapping(source = "coach", target = "currentCoach")
    @Mapping(source = "squad", target = "currentPlayers")


    Team toTeamEntity(TeamExternalResponse teamExternal);
}
