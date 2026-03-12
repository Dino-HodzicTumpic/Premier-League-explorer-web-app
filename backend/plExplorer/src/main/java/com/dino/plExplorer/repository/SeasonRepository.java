package com.dino.plExplorer.repository;

import com.dino.plExplorer.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeasonRepository extends JpaRepository<Season, Long> {

    public Optional<Season> findByName(String name);
}
