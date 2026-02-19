package com.dino.plExplorer.repository;


import com.dino.plExplorer.entity.Coach;
import org.springframework.data.jpa.repository.JpaRepository;



public interface CoachRepository extends JpaRepository<Coach, Long> {
}
