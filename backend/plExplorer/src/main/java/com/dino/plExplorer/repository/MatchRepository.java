package com.dino.plExplorer.repository;

import com.dino.plExplorer.entity.Match;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {
    Optional<Match> findByExternalId(Long externalId);
    Optional<Match> findByEspnId(String espnId);

    @Query("SELECT m FROM Match m LEFT JOIN FETCH m.goals  WHERE m.espnId = :espnId")
     Optional<Match> findWithGoals(@Param("espnId") String espnMatchId  );

    @Query("SELECT m FROM Match m LEFT JOIN FETCH m.bookings  WHERE m.espnId = :espnId")
    Optional<Match> findWithBookings(@Param("espnId") String espnMatchId  );

    @Query("SELECT m FROM Match m LEFT JOIN FETCH m.substitutions  WHERE m.espnId = :espnId")
    Optional<Match> findWithSubs(@Param("espnId") String espnMatchId  );

    @Query("SELECT m FROM Match m LEFT JOIN FETCH m.statistics WHERE m.espnId = :espnId")
    Optional<Match> findWithStats(@Param("espnId") String espnMatchId  );

    @Query("SELECT m FROM Match m LEFT JOIN FETCH m.referees  WHERE m.espnId = :espnId")
    Optional<Match> findWithReferees(@Param("espnId") String espnMatchId  );

    @Query("SELECT m FROM Match m LEFT JOIN FETCH m.appearances  WHERE m.espnId = :espnId")
    Optional<Match> findWithAppearances(@Param("espnId") String espnMatchId  );

    @Query("""
            SELECT m FROM Match m join m.season s WHERE m.matchday = :gameweek
             AND YEAR(s.startDate) = :seasonStartYear""")
    List<Match> findByGameweekAndSeasonStartYear(@Param("gameweek") Integer gameweek, @Param("seasonStartYear") Integer seasonStartYear);
}
