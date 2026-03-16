package com.dino.plExplorer.service.topScorers;

import com.dino.plExplorer.dto.external.footballdata.topScorers.TopScorersResponse;
import com.dino.plExplorer.dto.response.topScorers.TopScorersDto;
import com.dino.plExplorer.entity.Player;
import com.dino.plExplorer.mapper.topScorers.TopScorersMapper;
import com.dino.plExplorer.projection.PlayerImageProjection;
import com.dino.plExplorer.repository.PlayerRepository;
import com.dino.plExplorer.service.FootballDataApiService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class TopScorersService {
    private final FootballDataApiService footballDataApiService;
    private final TopScorersMapper topScorersMapper;
    private final PlayerRepository playerRepository;

    public List<TopScorersDto> getTopScorers(String season, Integer limit) {
        Optional<TopScorersResponse> external = footballDataApiService.fetchTopScorers(season, limit);

        if (external.isEmpty()) {
            log.error("Football data API service returned empty response");
            return List.of();
        }

        List<TopScorersDto> topScorers = topScorersMapper.toDtoList(external.get());
        enrichWithPlayerImages(topScorers);

        return topScorers;
    }

    /**
     * Mutates the provided list in-place by setting playerImageUrl on each dto.
     */
    private void enrichWithPlayerImages(List<TopScorersDto> topScorers) {
        Set<Long> externalIds = topScorers.stream()
                .map(TopScorersDto::getExternalId)
                .collect(Collectors.toSet());

        if (externalIds.isEmpty()) {
            return;
        }

        List<PlayerImageProjection> playersWithImages = playerRepository.findAllByExternalIdIn(externalIds);
        Map<Long, String> playerImageByExternalId = playerRepository.findAllByExternalIdIn(externalIds).stream()
                .collect(HashMap::new, (map, proj) -> map.put(proj.getExternalId(), proj.getImageUrl()), HashMap::putAll);

        topScorers.forEach(dto -> dto.setPlayerImageUrl(playerImageByExternalId.get(dto.getExternalId())));
    }
}
