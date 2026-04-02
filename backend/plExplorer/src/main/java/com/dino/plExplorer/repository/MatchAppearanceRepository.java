package com.dino.plExplorer.repository;

import com.dino.plExplorer.entity.Match;
import com.dino.plExplorer.entity.MatchAppearance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchAppearanceRepository extends JpaRepository<MatchAppearance,Long> {
    void deleteByMatch(Match match);
}
