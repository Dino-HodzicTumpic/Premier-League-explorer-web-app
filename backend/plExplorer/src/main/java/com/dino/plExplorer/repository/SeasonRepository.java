package com.dino.plExplorer.repository;

import com.dino.plExplorer.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SeasonRepository extends JpaRepository<Season, Long> {

     Optional<Season> findByName(String name);
     Season findByIsCurrentTrue();
     @Query("Select s from Season s where YEAR(s.startDate) = :year")
     Season findByStartYear(  @Param("year") Integer startYear);
}
