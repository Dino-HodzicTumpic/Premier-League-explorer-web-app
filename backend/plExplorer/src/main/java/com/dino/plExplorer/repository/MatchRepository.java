package com.dino.plExplorer.repository;

import com.dino.plExplorer.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {
    Optional<Match> findByExternalId(Long externalId);
    Optional<Match> findByEspnId(String espnId);
    @Query("""
            SELECT m FROM Match m join m.season s WHERE m.matchday = :gameweek
             AND YEAR(s.startDate) = :seasonStartYear""")
    List<Match> findByGameweekAndSeasonStartYear(@Param("gameweek") Integer gameweek, @Param("seasonStartYear") Integer seasonStartYear);
}
