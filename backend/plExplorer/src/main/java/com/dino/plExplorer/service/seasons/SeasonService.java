package com.dino.plExplorer.service.seasons;

import com.dino.plExplorer.repository.SeasonRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class SeasonService {

    private SeasonRepository seasonRepository;

    public Integer getCurrentSeason() {
        return seasonRepository.findByIsCurrentTrue().getStartDate().getYear();
    }

}
