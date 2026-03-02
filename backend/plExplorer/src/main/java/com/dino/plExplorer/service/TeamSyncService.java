package com.dino.plExplorer.service;

import com.dino.plExplorer.entity.Team;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class TeamSyncService {

    private final TeamDataProcessingService teamDataProcessingService;
    private final TeamPersistenceService teamPersistenceService;

    @Transactional
    public void syncPremierLeagueTeams() {
        log.info("Starting Premier League teams sync process");

        // 1. Dohvati timove iz API-ja i obogati njihove podatke
        List<Team> processedTeams = teamDataProcessingService.processAndEnrichTeams();

        // 2. Spremi timove i igrace u bazu i dohvati slike igraca i spremi i njih
        List<Team> savedTeams = teamPersistenceService.saveTeamsWithPlayersAndImages(processedTeams);

        log.info("Premier League sync completed. Saved {} teams", savedTeams.size());
    }
}
