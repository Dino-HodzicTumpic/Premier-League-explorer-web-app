package com.dino.plExplorer.scheduler;


import com.dino.plExplorer.dto.external.espn.summary.EspnSummaryResponse;
import com.dino.plExplorer.dto.response.matches.LiveMatchDetailsSnapshotDto;
import com.dino.plExplorer.mapper.matches.LiveMatchDetailsSnapshotMapper;
import com.dino.plExplorer.service.espn.EspnApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class LiveMatchDetailsScheduler {

    private final CacheManager cacheManager;
    private final EspnApiService espnApiService;
    private final LiveMatchDetailsSnapshotMapper liveMatchDetailsSnapshotMapper;

    @Scheduled(fixedDelay =60000 )
    public void refreshLiveMatchesDetails(){

        CaffeineCache basicCache = (CaffeineCache) cacheManager.getCache("liveMatchDetailsCache");

        if(basicCache==null){
            log.warn("Live match list cache not found");
            return;
        }

        Cache detailsCache = cacheManager.getCache("liveMatchDetailsCache");

        if(detailsCache==null){
            log.warn("Live match details cache not found");
            return;
        }

        basicCache.getNativeCache().asMap().keySet().forEach(espnMatchId -> {
            String matchId = (String) espnMatchId;
            Optional<EspnSummaryResponse> summary = espnApiService.fetchEventSummary(matchId);

            if(summary.isEmpty()){
                log.warn("Could not fetch summary for matchId {}", matchId);
                return;
            }

            LiveMatchDetailsSnapshotDto snapshot = liveMatchDetailsSnapshotMapper.toSnapshot(summary.get(), matchId);

            detailsCache.put(matchId, snapshot);
        });
    }

}
