package com.dino.plExplorer.service.matches;

import com.dino.plExplorer.dto.external.footballdata.matches.MatchesResponse;
import com.dino.plExplorer.dto.response.matches.MatchDto;
import com.dino.plExplorer.mapper.matches.MatchesMapper;
import com.dino.plExplorer.service.FootballDataApiService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class MatchesService {
    private final FootballDataApiService footballDataApiService;
    private final MatchesMapper matchesMapper;

    public List<MatchDto> getMatches(int gameweek){
     Optional<MatchesResponse> external = footballDataApiService.getMatchesByGameWeek(gameweek);

        if(external.isEmpty()){
            log.error("Football data API service returned empty response for matches");
            return List.of();
        }

        return matchesMapper.toDtoList(external.get());
    }

    //TODO: implementirati dohvaćanje trenutnog gameweeka iz baze ili API-ja
    public Optional<Integer> getCurrentGameWeek(){
       return footballDataApiService.getCurrentGameWeekNumber();
    }

}
