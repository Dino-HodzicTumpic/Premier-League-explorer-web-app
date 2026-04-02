package com.dino.plExplorer.repository;

import com.dino.plExplorer.entity.Booking;
import com.dino.plExplorer.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;



public interface BookingRepository extends JpaRepository<Booking, Long> {
    void deleteByMatch(Match match);
}
