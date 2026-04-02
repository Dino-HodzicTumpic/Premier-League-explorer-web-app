package com.dino.plExplorer.repository;

import com.dino.plExplorer.entity.Match;
import com.dino.plExplorer.entity.MatchReferee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRefereeRepository extends JpaRepository<MatchReferee, Long> {
    void deleteByMatch(Match match);
}
