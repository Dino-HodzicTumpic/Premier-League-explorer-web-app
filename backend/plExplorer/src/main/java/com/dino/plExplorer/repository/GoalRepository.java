package com.dino.plExplorer.repository;

import com.dino.plExplorer.entity.Goal;
import com.dino.plExplorer.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    void deleteByMatch(Match match);
}
