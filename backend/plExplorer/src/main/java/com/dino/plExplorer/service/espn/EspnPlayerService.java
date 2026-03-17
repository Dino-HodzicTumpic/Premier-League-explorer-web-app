package com.dino.plExplorer.service.espn;

import com.dino.plExplorer.dto.external.espn.EspnPlayerDto;
import com.dino.plExplorer.dto.external.espn.EspnPlayersResponseDto;
import com.dino.plExplorer.entity.Player;
import com.dino.plExplorer.projection.TeamEspnIdProjection;
import com.dino.plExplorer.repository.PlayerRepository;
import com.dino.plExplorer.repository.TeamRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class EspnPlayerService {
    private final EspnApiService espnApiService;
    private final TeamRepository teamRepository;
    private  final PlayerRepository playerRepository;

    public void syncPlayersWithEspnApi() {
        log.info("Starting ESPN player data synchronization...");


        List<TeamEspnIdProjection> teams = teamRepository.findAllBy();

        teams.forEach(team -> syncPlayersForTeam(team.getEspnId()));

        log.info("ESPN player data synchronization completed successfully.");

    }

    private void syncPlayersForTeam(String teamEspnId){
        try {

            Optional<EspnPlayersResponseDto> responseOpt = espnApiService.fetchPlayers(teamEspnId);
            if (responseOpt.isEmpty()) {
                log.warn("No player data received from ESPN API.");
                return;
            }

            List<EspnPlayerDto> espnPlayers = responseOpt.get().getPlayers();

            //izvuci sva imena jer po tome spajamo
            List<String> fullNames = espnPlayers.stream()
                    .map(EspnPlayerDto::getFullName)
                    .toList();

            List<String> displayNames = espnPlayers.stream()
                    .map(EspnPlayerDto::getDisplayName)
                    .toList();

            List<Player> dbPlayers = playerRepository.findByNameInOrNameIn(fullNames,displayNames);

            Map<String,Player> playerMap = dbPlayers.stream().collect(Collectors.toMap(
                    p -> p.getName(),
                    p -> p
            ));

            // add espnIds to players
            espnPlayers.forEach(espnPlayer -> {
                Player player = playerMap.get(espnPlayer.getFullName());
                if(player == null){
                    player = playerMap.get(espnPlayer.getDisplayName());
                }

                    if(player != null){
                        player.setEspnId(espnPlayer.getId());
                        log.info("Updated ESPN ID for player: {} {}", player.getFirstName(), player.getLastName());
                    } else {
                        log.warn("No matching player found in database for ESPN player: {} {}", espnPlayer.getFullName(), espnPlayer.getDisplayName());
                    }
            });


                playerRepository.saveAll(dbPlayers);

        } catch (Exception e) {
            log.error("Error during ESPN player data synchronization: {}", e.getMessage(), e);
        }
    }
}
