package com.dino.plExplorer.scheduler;

import com.dino.plExplorer.dto.external.espn.scoreboard.EspnDetail;
import com.dino.plExplorer.dto.external.espn.scoreboard.EspnEvent;
import com.dino.plExplorer.dto.external.espn.scoreboard.EspnScoreboardResponse;
import com.dino.plExplorer.dto.external.espn.summary.EspnSummaryResponse;
import com.dino.plExplorer.dto.response.matches.LiveMatchSnapshotDto;
import com.dino.plExplorer.service.espn.EspnApiService;
import com.dino.plExplorer.service.matches.MatchFinalizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class LiveMatchListScheduler {

    private final CacheManager cacheManager;
    private final EspnApiService espnApiService;
    private final MatchFinalizationService matchFinalizationService;

    @Scheduled(fixedDelay = 20000)
    public void refreshLiveMatchesList(){
     Optional<EspnScoreboardResponse> scoreboardOpt = espnApiService.fetchScoreboardForToday();

     if (scoreboardOpt.isEmpty()) {
         log.warn("Failed to fetch today's scoreboard data from ESPN API. Live matches list will not be updated.");
         return;
     }

     Cache cache = cacheManager.getCache("liveMatchListCache");

     if (cache == null) {
            log.error("LiveMatchListCache not found. Cannot update live matches list.");
            return;
        }

     //separate liveGames and Rest of the Games
        Map<Boolean, List<EspnEvent>> partitioned = scoreboardOpt.get().getEvents().stream()
                .collect(Collectors.partitioningBy(event ->
                        event.getCompetitions().getFirst().getStatus().getType().getState().equals("in")
                ));

        List<EspnEvent> liveGames =  partitioned.get(true);
        List<EspnEvent> otherGames = partitioned.get(false);


        // cache live games data
        liveGames.forEach(event -> {
            LiveMatchSnapshotDto snapshot = LiveMatchSnapshotDto.builder()
                    .espnId(event.getEspnId())
                    .minute(resolveMinute(event).orElse(null))
                    .injuryTime(resolveInjuryTime(event).orElse(null))
                    .homeScore(resolveHomeScore(event))
                    .awayScore(resolveAwayScore(event))
                    .build();

            cache.put(event.getEspnId(), snapshot);
        });

        //remove finsihed games from cache and save them in db
        otherGames.forEach(event -> {
            LiveMatchSnapshotDto cached = cache.get(event.getEspnId(), LiveMatchSnapshotDto.class);

            if (cached != null){
                // call method that will save them in db and remove them from cache
                matchFinalizationService.finalizeMatch(event.getEspnId());
            }
        });



    }

    private Optional<Integer> resolveMinute(EspnEvent event){
        String minuteStr = event.getCompetitions().getFirst().getStatus().getType().getDetail();


        String[] plusSplit = minuteStr.split("\\+");
        String base = plusSplit[0].replace("'", "").trim();
        try {
            return Optional.of(Integer.parseInt(base));
        } catch (NumberFormatException ignored) {
            return Optional.empty();
        }
    }

    private Optional<Integer> resolveInjuryTime(EspnEvent event){
        String minuteStr = event.getCompetitions().getFirst().getStatus().getType().getDetail();


        if (!minuteStr.contains("+")) {
            return Optional.empty();
        }
        String plusPart = minuteStr.substring(minuteStr.indexOf('+') + 1).replace("'", "").trim();
        try {
            return Optional.of(Integer.parseInt(plusPart));
        } catch (NumberFormatException ignored) {
            return Optional.empty();
        }
    }

    private Integer resolveScore(EspnEvent event, String homeOrAway){
        String homeScoreStr = event.getCompetitions().getFirst().getCompetitors()
                .stream()
                .filter(team -> team.getHomeAway().equalsIgnoreCase(homeOrAway))
                .findFirst()
                .map(team -> team.getScore()).orElse(null);

        return Integer.parseInt(homeScoreStr);
    }

    private Integer resolveHomeScore(EspnEvent event){
        return resolveScore(event, "home");
    }

    private Integer resolveAwayScore(EspnEvent event){
        return resolveScore(event, "away");
    }



}


