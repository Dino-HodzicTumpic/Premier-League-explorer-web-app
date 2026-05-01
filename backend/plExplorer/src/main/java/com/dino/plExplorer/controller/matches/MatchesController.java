package com.dino.plExplorer.controller.matches;

import com.dino.plExplorer.dto.response.matches.MatchDto;
import com.dino.plExplorer.dto.response.matches.MatchListDto;
import com.dino.plExplorer.service.matches.MatchesService;
import com.dino.plExplorer.service.seasons.SeasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matches")
public class MatchesController {
    private final MatchesService matchesService;
    private final SeasonService seasonService;

    @GetMapping
    public List<MatchListDto> getMatches(@RequestParam Integer gameweek, @RequestParam(required = false) Integer season) {
        if (season == null) {
            season = seasonService.getCurrentSeason();
        }

        return matchesService.getMatches(gameweek, season);
    }

    @GetMapping("/current-gameweek")
    public Integer getCurrentGameWeek(){
        return matchesService.getCurrentGameWeek()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Current gameweek not found"));
    }


}
