package com.dino.plExplorer.repository;

import com.dino.plExplorer.entity.UserFavoritePlayer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFavoritePlayerRepository extends JpaRepository<UserFavoritePlayer, Long> {
}
