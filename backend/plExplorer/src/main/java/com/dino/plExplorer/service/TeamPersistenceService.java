package com.dino.plExplorer.service;

import com.dino.plExplorer.dto.ImageUploadResult;
import com.dino.plExplorer.entity.Coach;
import com.dino.plExplorer.entity.Team;
import com.dino.plExplorer.entity.Player;
import com.dino.plExplorer.entity.interfaces.HasImage;
import com.dino.plExplorer.repository.TeamRepository;
import com.dino.plExplorer.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class TeamPersistenceService {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final ImageService imageService;
    private final SportsDbApiService sportsDbApiService;

   @Transactional
    public List<Team> saveTeamsWithPlayersAndImages(List<Team> teams){
       log.info("Saving {} teams to database", teams.size());
       List<Team> savedTeams = teamRepository.saveAll(teams);

       savedTeams.forEach(team -> {
           //coach
           Coach currentCoach = team.getCurrentCoach();
           if(currentCoach != null){
               currentCoach.setCurrentTeam(team);
               //uploadImage(currentCoach, "coaches");
           }
            //players
            List<Player> currentPlayers = team.getCurrentPlayers();
           if(currentPlayers != null) {
               currentPlayers.forEach(player -> {
                   player.setCurrentTeam(team);
                   // upload slike
                  // uploadImage(player, "players");
               });
           }
       });
       log.info("Successfully saved {} teams", savedTeams.size());
       return savedTeams;
   }

}
