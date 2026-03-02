package com.dino.plExplorer.service;

import com.dino.plExplorer.dto.ImageUploadResult;
import com.dino.plExplorer.entity.Player;
import com.dino.plExplorer.entity.interfaces.HasImage;
import com.dino.plExplorer.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class PlayerImageSyncService {
    private final ImageService imageService;
    private  final SportsDbApiService sportsDbApiService;
    private final PlayerRepository playerRepository;

    public void updateAllPlayerImages(){
        List<Player> players = playerRepository.findAll();

        players.forEach(player ->{
            if(uploadImage(player, "players")){
                playerRepository.save(player);
            }
        } );
    }



    private <T extends HasImage> boolean uploadImage(T entity, String folder){
        Optional<String> imageUrl = sportsDbApiService.fetchImage(entity.getName());

        // sleep/pauza 4 sekund da ne predjemo api limit
        try {
            Thread.sleep(4000); // 4 sekunde
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thread interrupted while sleeping between API calls", e);
            return false;
        }

        if(imageUrl.isEmpty()) return false;

        Optional<ImageUploadResult> result = imageService.uploadImage(imageUrl.get(), folder);

        if (result.isEmpty()) return false;

        entity.setImagePublicId(result.get().publicId());
        entity.setImageUrl(result.get().secureUrl());

        return true;
    }
}
