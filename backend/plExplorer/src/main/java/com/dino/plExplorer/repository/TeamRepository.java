package com.dino.plExplorer.repository;

import com.dino.plExplorer.entity.Team;
import com.dino.plExplorer.projection.TeamEspnIdProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByExternalId(Long externalId);
    Optional<Team> findByEspnId(String espnId);
    List<TeamEspnIdProjection> findAllBy();
}
