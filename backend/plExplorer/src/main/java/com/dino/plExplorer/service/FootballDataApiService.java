package com.dino.plExplorer.service;

import com.dino.plExplorer.dto.external.footballdata.initseed.PlayerExternalData;
import com.dino.plExplorer.dto.external.footballdata.initseed.TeamExternalResponse;
import com.dino.plExplorer.dto.external.footballdata.initseed.TeamsResponse;
import com.dino.plExplorer.dto.external.footballdata.standings.StandingsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j

public class FootballDataApiService {


    private final WebClient footballDataWebClient;

    public FootballDataApiService(@Qualifier("footballDataWebClient") WebClient footballDataWebClient){
        this.footballDataWebClient = footballDataWebClient;
    }

    /**
      Dohvaća sve timove Premier League lige s njihovim igračima
     */
    public List<TeamExternalResponse> fetchPremierLeagueTeams() {
        log.info("Fetching Premier League teams from Football-data.org API");

        try{
            TeamsResponse response = footballDataWebClient
                                    .get()
                                    .uri("/competitions/PL/teams")
                                    .retrieve()
                                    .bodyToMono(TeamsResponse.class)
                                    .block();

            if(response == null || response.getTeams() == null){
                log.warn("Received empty response for Premier League teams");
                return List.of();
            }

            log.info("Successfully fetched {} teams", response.getTeams().size());
            return response.getTeams();

        } catch(Exception e) {
            log.error("Error while fetching Premier League teams", e);
            throw new RuntimeException("Failed to fetch teams from Football Data API", e);
        }

    }

    /**
     Dohvaća dodatnu informaciju o pojedinom igraču koja nam fali (broj dresa)
     */
    public PlayerExternalData fetchPlayerDetails(Long playerId) {
        log.info("Fetching details for player with id: {}", playerId);

        try{
            PlayerExternalData response = footballDataWebClient
                    .get()
                    .uri( "/persons/{id}", playerId)
                    .retrieve()
                    .bodyToMono( PlayerExternalData.class)
                    .block();

            if (response == null) {
                log.warn("Received empty response for player id: {}", playerId);
                return null;
            }

            log.info("Successfully fetched details for player id: {}", playerId);
            return response;

        } catch (Exception e) {
            log.error("Error while fetching player details for player id: {}", playerId, e);
            throw new RuntimeException(e);
        }
    }

    public Optional<StandingsResponse> fetchStandings(){
        log.info("Fetching standings from Football-data.org API");
        try{
            StandingsResponse response = footballDataWebClient
                                        .get()
                                        .uri("/competitions/PL/standings")
                                        .retrieve()
                                        .bodyToMono(StandingsResponse.class)
                                        .block();

          return  Optional.ofNullable(response);

        } catch (Exception e){
            log.error("Error while fetching standings from Football-data.org API", e);
            return Optional.empty();
        }
    }
}
