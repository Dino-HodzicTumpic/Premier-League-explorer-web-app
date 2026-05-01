package com.dino.plExplorer.mapper.matches;

import com.dino.plExplorer.config.mapper.MapStructConfig;
import com.dino.plExplorer.dto.response.matches.MatchListDto;
import com.dino.plExplorer.entity.Match;
import com.dino.plExplorer.mapper.DateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapStructConfig.class, uses = { DateMapper.class, MatchesMapper.class})
public interface MatchListDtoMapper {

    @Mapping(target = "matchId", source = "id")
    @Mapping(target = "espnMatchId", source = "espnId")
    @Mapping(target = "kickoffTime", source = "utcDate" , qualifiedByName = "toOffsetDateTime")
    MatchListDto toDto(Match match);

    List<MatchListDto> toDtoList(List<Match> matches);
}
