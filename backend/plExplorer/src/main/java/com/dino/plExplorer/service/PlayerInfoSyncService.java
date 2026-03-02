package com.dino.plExplorer.service;

import com.dino.plExplorer.dto.external.footballdata.initseed.PlayerExternalData;
import com.dino.plExplorer.entity.Player;
import com.dino.plExplorer.mapper.PlayerExternalMapper;
import com.dino.plExplorer.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class PlayerInfoSyncService {
    private final FootballDataApiService footballDataApiService;
    private final PlayerRepository playerRepository;
    private final PlayerExternalMapper playerExternalMapper;


    public void updatePlayersInfo(){
        List<Player> players = playerRepository.findAll();

        players.forEach(this::updatePlayerInfo);
    }

    private boolean updatePlayerInfo(Player player){
        PlayerExternalData playerResponse = footballDataApiService.fetchPlayerDetails(player.getExternalId());

        // sleep/pauza 7 sekundi da ne predjemo api limit
        try {
            Thread.sleep(7000); // 7 sekundi
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thread interrupted while sleeping between API calls", e);
            return false;
        }

        if(playerResponse == null){
            log.warn("PlayerExternalData null for player id {}", player.getExternalId());
            return false;
        }

        Player mappedPlayer = playerExternalMapper.toPlayerEntity(playerResponse);

        player.setShirtNumber(mappedPlayer.getShirtNumber());
        player.setContractStart(mappedPlayer.getContractStart());
        player.setContractUntil(mappedPlayer.getContractUntil());
        playerRepository.save(player);

        return true;
    }
}
