package com.dino.plExplorer.controller.topScorers;

import com.dino.plExplorer.dto.response.topScorers.TopScorersDto;
import com.dino.plExplorer.service.topScorers.TopScorersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/top-scorers")
@RequiredArgsConstructor
public class TopScorersController {
    private final TopScorersService topScorersService;

    @GetMapping
    public List<TopScorersDto> getTopScorers(@RequestParam String season, @RequestParam(defaultValue = "10") int limit)
    {
        return topScorersService.getTopScorers(season, limit);
    }
}
