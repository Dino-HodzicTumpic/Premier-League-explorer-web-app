package com.dino.plExplorer.service;

import com.dino.plExplorer.dto.external.footballdata.initseed.TeamExternalResponse;
import com.dino.plExplorer.entity.Team;
import com.dino.plExplorer.mapper.SquadMemberExternalMapper;
import com.dino.plExplorer.mapper.TeamExternalMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class TeamDataProcessingService {

    private final FootballDataApiService footballDataApiService;
    private final TeamExternalMapper teamExternalMapper;
    private final SquadMemberExternalMapper squadMemberExternalMapper;

    public List<Team> processAndEnrichTeams() {
        log.info("Processing and enriching Premier League teams data");

        //dohvati sve timove iz API-ja
        List<TeamExternalResponse> teamsFromApi = footballDataApiService.fetchPremierLeagueTeams();

        //za svakog igraca jos dohvati njegov broj dresa
      /* List<TeamExternalResponse> enrichedTeamsFromApi =  teamsFromApi.stream()
               .map(this::enrichTeamWithPlayerDetails)
               .toList(); */

       // mapiranje u entitete
      /*  List<Team> teams = enrichedTeamsFromApi.stream()
                .map(teamExternalMapper::toTeamEntity)
                .toList(); */

        // mapiranje u entitete
        List<Team> teams = teamsFromApi.stream()
                .map(teamExternalMapper::toTeamEntity)
                .toList();

        log.info("Successfully processed {} teams", teams.size());
        return teams;

    }

//    private TeamExternalResponse enrichTeamWithPlayerDetails(TeamExternalResponse team) {
//        if(team.getSquad() == null){
//            return team;
//        }
//
//        List<SquadMemberExternalData> enrichedSquad =  team.getSquad().stream()
//                .map(this::enrichPlayerData)
//                .toList();
//
//        team.setSquad(enrichedSquad);
//        return team;
//    }
//
//    private SquadMemberExternalData enrichPlayerData(SquadMemberExternalData squadMember){
//
//        SquadMemberExternalData playerDetails = footballDataApiService.fetchPlayerDetails(squadMember.getId());
//
//        if(playerDetails != null && playerDetails.getShirtNumber() != null){
//                squadMember.setShirtNumber(playerDetails.getShirtNumber());
//        }
//        // sleep/pauza 7 sekundi da ne predjemo api limit
//        try {
//            Thread.sleep(7000); // 7 sekundi
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            log.error("Thread interrupted while sleeping between API calls", e);
//        }
//
//        return squadMember;
//    }
}
