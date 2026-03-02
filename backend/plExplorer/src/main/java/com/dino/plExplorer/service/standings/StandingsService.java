package com.dino.plExplorer.service.standings;

import com.dino.plExplorer.dto.external.footballdata.standings.StandingsResponse;
import com.dino.plExplorer.dto.response.standings.TeamStandingDto;
import com.dino.plExplorer.mapper.standings.StandingsMapper;
import com.dino.plExplorer.service.FootballDataApiService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class StandingsService {
    private final FootballDataApiService footballDataApiService;
    private final StandingsMapper standingsMapper;

    public List<TeamStandingDto> getStandings(){
        Optional<StandingsResponse> external = footballDataApiService.fetchStandings();

        if(external.isEmpty()){
            log.error("Football data API service returned empty response");
            return new ArrayList<>();
        }

        return standingsMapper.toDtoList(
            external.get().getStandings().getFirst().getTable()
        );
    }
}
