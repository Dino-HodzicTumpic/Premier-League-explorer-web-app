package com.dino.plExplorer.bootstrap;

import com.dino.plExplorer.service.PlayerImageSyncService;
import com.dino.plExplorer.service.PlayerInfoSyncService;
import com.dino.plExplorer.service.TeamSyncService;
import com.dino.plExplorer.service.espn.EspnPlayerService;
import com.dino.plExplorer.service.standings.StandingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("seed") // samo u dev okruženju
@RequiredArgsConstructor
@Slf4j
public class DevDataSeeder {

    private final TeamSyncService teamSyncService;
    private final PlayerImageSyncService  playerImageSyncService;
    private final PlayerInfoSyncService playerInfoSyncService;
    private final StandingsService standingsService;
    private final EspnPlayerService espnPlayerService;

    @EventListener(ApplicationReadyEvent.class)
    public void seedDevData() {
        log.info("Seeding dev data: Starting Premier League teams sync...");

        try {
           // teamSyncService.syncPremierLeagueTeams();
            //playerImageSyncService.updateAllPlayerImages();
            //playerInfoSyncService.updatePlayersInfo();
            //standingsService.fetchAndSavePreviousSeasonsStandings();
            //espnPlayerService.syncPlayersWithEspnApi();

            log.info("Seeding dev data: Premier League teams sync completed.");
        } catch (Exception e) {
            log.error("Error during dev data seeding", e);
        }
    }
}