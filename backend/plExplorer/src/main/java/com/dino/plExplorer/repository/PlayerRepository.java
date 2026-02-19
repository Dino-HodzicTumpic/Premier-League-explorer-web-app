package com.dino.plExplorer.repository;

import com.dino.plExplorer.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
