package com.dino.plExplorer.repository;

import com.dino.plExplorer.entity.UserFavoriteTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFavoriteTeamRepository extends JpaRepository<UserFavoriteTeam, Long> {
}
