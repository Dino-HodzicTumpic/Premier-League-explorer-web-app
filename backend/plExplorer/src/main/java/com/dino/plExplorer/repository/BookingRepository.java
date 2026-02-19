package com.dino.plExplorer.repository;

import com.dino.plExplorer.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;



public interface BookingRepository extends JpaRepository<Booking, Long> {
}
