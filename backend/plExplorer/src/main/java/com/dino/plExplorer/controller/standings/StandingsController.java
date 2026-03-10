package com.dino.plExplorer.controller.standings;

import com.dino.plExplorer.dto.response.standings.TeamStandingDto;
import com.dino.plExplorer.service.standings.StandingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/standings")
@RequiredArgsConstructor
@CrossOrigin(origins ={ "http://localhost:5174", "http://localhost:5173"})
public class StandingsController {

    private final StandingsService standingsService;

    @GetMapping
    public List<TeamStandingDto> getStandings(){
        return standingsService.getStandings();
    }
}
