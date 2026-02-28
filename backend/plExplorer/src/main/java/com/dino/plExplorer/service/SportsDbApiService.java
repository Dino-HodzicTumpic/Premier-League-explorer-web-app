package com.dino.plExplorer.service;

import com.dino.plExplorer.dto.external.sportsDb.SportsDbPlayerResponse;
import com.dino.plExplorer.entity.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@Slf4j
public class SportsDbApiService {

    private final WebClient theSportsDbWebClient;

    public SportsDbApiService(@Qualifier("theSportsDbWebClient") WebClient theSportsDbWebClient){
        this.theSportsDbWebClient = theSportsDbWebClient;
    }

    // dohvaca i za coach i za player
    public Optional<String> fetchImage(String name){
        Optional<String> searchQuery = buildSearchQuery(name);

        if(searchQuery.isEmpty()){
            log.warn("Cannot build search query for player/coach: {}", name);
            return Optional.empty();
        }

        try {
          SportsDbPlayerResponse response =  theSportsDbWebClient.get()
                    .uri(searchQuery.get())
                    .retrieve()
                  .bodyToMono(SportsDbPlayerResponse.class)
                  .block();

          if(response == null || response.getPlayer() == null ){
              return Optional.empty();
          }
            String cutoutUrl = response.getPlayer().getFirst().getStrCutout();
            return Optional.ofNullable(cutoutUrl);

        } catch (Exception e) {
            log.error("Error while fetching Player/coach image, " + name, e);
            return Optional.empty();
        }

    }


    /**
     * Gradi URL path za TheSportsDB API pretragu igrača.
     * Format: /searchplayers.php?p=FirstName_LastName
     * Primjer: "Bukayo Saka" -> "/searchplayers.php?p=Bukayo_Saka"
     *
     * @param playerName Puno ime igrača
     * @return URL path za API poziv
     */
    private Optional<String> buildSearchQuery(String playerName){


        if(playerName == null || !playerName.contains(" ")) {
            return Optional.empty();
        }

        String formattedName = playerName.replace(" ", "_");
        return Optional.of("/searchplayers.php?p=" + formattedName);

    }

}
