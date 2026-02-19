package com.dino.plExplorer.repository;

import com.dino.plExplorer.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
