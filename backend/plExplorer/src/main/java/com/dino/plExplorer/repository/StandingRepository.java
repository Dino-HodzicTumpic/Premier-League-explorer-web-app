package com.dino.plExplorer.repository;

import com.dino.plExplorer.entity.Standing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StandingRepository extends JpaRepository<Standing, Long> {
    List<Standing> findBySeason_NameOrderByPositionAsc(String seasonName);
}
