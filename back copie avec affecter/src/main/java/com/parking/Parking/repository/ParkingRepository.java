package com.parking.Parking.repository;

import com.parking.Parking.entities.Parking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingRepository extends JpaRepository <Parking, Long> {
    boolean existsByPosition(String position);
    boolean existsByPositionAndIdNot(String position, Long userId);

    Parking findByPosition(String position);
}
