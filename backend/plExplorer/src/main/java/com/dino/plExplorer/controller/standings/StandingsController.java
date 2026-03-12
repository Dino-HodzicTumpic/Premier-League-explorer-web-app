package com.dino.plExplorer.controller.standings;

import com.dino.plExplorer.dto.response.standings.TeamStandingDto;
import com.dino.plExplorer.service.standings.StandingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/standings")
@RequiredArgsConstructor
public class StandingsController {

    private final StandingsService standingsService;

    @GetMapping("/current")
    public List<TeamStandingDto> getCurrentStandings(){
        return standingsService.getCurrentStandings();
    }

    @GetMapping
    public List<TeamStandingDto> getSeasonStandings(@RequestParam String season){
    return standingsService.getSeasonStandings(season);
    }
}
