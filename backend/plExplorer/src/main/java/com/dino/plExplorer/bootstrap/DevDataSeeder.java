package com.dino.plExplorer.bootstrap;

import com.dino.plExplorer.service.PlayerImageSyncService;
import com.dino.plExplorer.service.PlayerInfoSyncService;
import com.dino.plExplorer.service.TeamSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev") // samo u dev okruženju
@RequiredArgsConstructor
@Slf4j
public class DevDataSeeder {

    private final TeamSyncService teamSyncService;
    private final PlayerImageSyncService  playerImageSyncService;
    private final PlayerInfoSyncService playerInfoSyncService;

    @EventListener(ApplicationReadyEvent.class)
    public void seedDevData() {
        log.info("Seeding dev data: Starting Premier League teams sync...");

        try {
           // teamSyncService.syncPremierLeagueTeams();
            //playerImageSyncService.updateAllPlayerImages();
            playerInfoSyncService.updatePlayersInfo();
            log.info("Seeding dev data: Premier League teams sync completed.");
        } catch (Exception e) {
            log.error("Error during dev data seeding", e);
        }
    }
}