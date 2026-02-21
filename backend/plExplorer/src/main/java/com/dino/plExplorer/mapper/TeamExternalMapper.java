package com.dino.plExplorer.mapper;

import com.dino.plExplorer.config.mapper.MapStructConfig;
import com.dino.plExplorer.dto.external.footballdata.TeamExternalResponse;
import com.dino.plExplorer.entity.Player;
import com.dino.plExplorer.entity.Team;
import jakarta.persistence.OneToMany;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface TeamExternalMapper {
    @Mapping(source = "id", target ="externalId" )
    @Mapping(source = "crest", target = "crestUrl")
    @Mapping(source = "founded", target = "yearFounded")
    @Mapping(source = "venue", target = "stadium")
    @Mapping(source = "coach", target = "currentCoach")
    @Mapping(source = "squad", target = "currentPlayers")


    Team toTeamEntity(TeamExternalResponse teamExternal);
}
