package com.dino.plExplorer.repository;

import com.dino.plExplorer.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {
    Optional<Match> findByExternalId(Long externalId);
    Optional<Match> findByEspnId(String espnId);
}
