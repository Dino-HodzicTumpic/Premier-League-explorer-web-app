package com.dino.plExplorer.service.espn;

import com.dino.plExplorer.config.EspnSiteConfig;
import com.dino.plExplorer.dto.external.espn.EspnPlayersResponseDto;
import com.dino.plExplorer.dto.external.espn.scoreboard.EspnScoreboardResponse;
import com.dino.plExplorer.dto.external.espn.summary.EspnSummaryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@Slf4j
public class EspnApiService {
private final WebClient espnSiteWebClient;

    public EspnApiService(@Qualifier("espnSiteWebClient") WebClient espnSiteWebClient) {
        this.espnSiteWebClient = espnSiteWebClient;

    }

    public Optional<EspnPlayersResponseDto> fetchPlayers(String teamId){
        try {
            EspnPlayersResponseDto response = espnSiteWebClient.get()
                    .uri("/teams/{teamId}/roster", teamId)
                    .retrieve()
                    .bodyToMono(EspnPlayersResponseDto.class)
                    .block();
            return Optional.ofNullable(response);
        } catch (Exception e) {
            log.error("Error fetching players from ESPN API: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<EspnScoreboardResponse> fetchScoreboardForDateRange(String startDate, String endDate){
        try {
            EspnScoreboardResponse response = espnSiteWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/scoreboard")
                            .queryParam("dates", startDate + "-" + endDate)
                            .build())
                    .retrieve()
                    .bodyToMono(EspnScoreboardResponse.class)
                    .block();

            log.info("Scoreboard data: {}", response);
            return Optional.ofNullable(response);

        } catch (Exception e) {
            log.error("Error fetching scoreboard from ESPN API: {}", e.getMessage());
            return Optional.empty();
        }

    }

    public Optional<EspnSummaryResponse> fetchEventSummary(String eventId){
        try {
            EspnSummaryResponse response = espnSiteWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/summary")
                            .queryParam("event", eventId)
                            .build())
                    .retrieve()
                    .bodyToMono(EspnSummaryResponse.class)
                    .block();
            return Optional.ofNullable(response);

        } catch (Exception e) {
            log.error("Error fetching event summary from ESPN API: {}", e.getMessage());
            return Optional.empty();
        }

    }


}

