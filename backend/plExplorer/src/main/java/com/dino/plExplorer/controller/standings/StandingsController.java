package com.dino.plExplorer.controller.standings;

import com.dino.plExplorer.dto.response.standings.TeamStandingDto;
import com.dino.plExplorer.service.standings.StandingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/standings")
@RequiredArgsConstructor
public class StandingsController {

    private final StandingsService standingsService;

    @GetMapping
    public List<TeamStandingDto> getStandings(){
        return standingsService.getStandings();
    }
}
