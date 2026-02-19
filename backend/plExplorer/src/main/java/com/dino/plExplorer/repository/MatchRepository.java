package com.dino.plExplorer.repository;

import com.dino.plExplorer.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
}
