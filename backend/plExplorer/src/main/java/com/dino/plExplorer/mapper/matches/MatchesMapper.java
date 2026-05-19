package com.dino.plExplorer.mapper.matches;

import com.dino.plExplorer.config.mapper.MapStructConfig;
import com.dino.plExplorer.dto.external.footballdata.matches.Match;
import com.dino.plExplorer.dto.external.footballdata.matches.MatchesResponse;
import com.dino.plExplorer.dto.external.footballdata.matches.Team;
import com.dino.plExplorer.dto.response.matches.MatchDto;
import com.dino.plExplorer.dto.response.matches.TeamDto;
import org.mapstruct.Mapper;

import java.util.Collections;
import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface MatchesMapper {

    MatchDto toDto(Match match);

    TeamDto toDto(Team team);

    MatchDto.ScoreDto toDto(Match.Score score);

    MatchDto.FullTimeDto toDto(Match.Score.FullTime fullTime);

    List<MatchDto> toDtoList(List<Match> matches);

    default List<MatchDto> toDtoList(MatchesResponse response) {
        if (response == null || response.getMatches() == null) {
            return Collections.emptyList();
        }
        return toDtoList(response.getMatches());
    }
}

