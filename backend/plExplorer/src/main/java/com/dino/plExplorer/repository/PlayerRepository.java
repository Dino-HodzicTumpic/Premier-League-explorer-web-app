package com.dino.plExplorer.repository;

import com.dino.plExplorer.entity.Player;
import com.dino.plExplorer.projection.PlayerImageProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<PlayerImageProjection> findAllByExternalIdIn(Collection<Long> externalIds);
    List<Player> findByNameInOrNameIn(List<String> fullNames, List<String> displayNames);
}
