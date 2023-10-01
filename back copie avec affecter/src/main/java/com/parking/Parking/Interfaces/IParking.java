package com.parking.Parking.Interfaces;

import com.parking.Parking.entities.Parking;

import java.util.List;
import java.util.Optional;

public interface IParking {


    List<Parking> getAllParkings();

    Optional<Parking> getParkingById(Long id);



    void deleteParking(long id);



    void addParking(Long userId, Parking parking);
}
