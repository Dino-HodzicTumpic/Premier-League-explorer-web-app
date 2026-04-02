package com.dino.plExplorer.repository;

import com.dino.plExplorer.entity.Match;
import com.dino.plExplorer.entity.MatchStatistic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchStatisticRepository extends JpaRepository<MatchStatistic, Long> {
	void deleteByMatch(Match match);
}
