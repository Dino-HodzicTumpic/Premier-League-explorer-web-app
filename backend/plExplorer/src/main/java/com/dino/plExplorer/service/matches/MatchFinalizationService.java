package com.dino.plExplorer.service.matches;

import com.dino.plExplorer.dto.external.espn.summary.EspnSummaryResponse;
import com.dino.plExplorer.service.espn.EspnApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchFinalizationService {

    private final CacheManager cacheManager;
    private final EspnApiService espnApiService;

    public void finalizeMatch(String espnMatchId) {
        // 1. Fresh pull direktno iz API-ja (ne iz cachea!)
            Optional<EspnSummaryResponse> summaryOpt = espnApiService.fetchEventSummary(espnMatchId);
            if (summaryOpt.isEmpty()){
                log.error("Failed to fetch match summary for match ID {}. Match finalization aborted.", espnMatchId);
                return;
            }

        // 2. Spremi sve u DB u jednoj transakciji
        // iskoristit postojecu logiku iz matchesSync i EspnUpsertService


        // 3. Tek onda očisti oba cachea
    }
}
