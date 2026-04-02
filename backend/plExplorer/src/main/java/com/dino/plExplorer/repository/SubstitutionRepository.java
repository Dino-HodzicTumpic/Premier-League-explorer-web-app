package com.dino.plExplorer.repository;

import com.dino.plExplorer.entity.Match;
import com.dino.plExplorer.entity.Substitution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubstitutionRepository extends JpaRepository<Substitution,Long> {
    void deleteByMatch(Match match);
}
