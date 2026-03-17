package com.dino.plExplorer.service.espn;

import com.dino.plExplorer.config.EspnSiteConfig;
import com.dino.plExplorer.dto.external.espn.EspnPlayersResponseDto;
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


}

