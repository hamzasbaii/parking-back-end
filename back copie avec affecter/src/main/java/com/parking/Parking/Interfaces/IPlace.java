package com.parking.Parking.Interfaces;

import com.parking.Parking.entities.Parking;
import com.parking.Parking.entities.Place;

import java.util.List;
import java.util.Optional;

public interface IPlace {

    List<Place> getAllPLaces();

    Optional<Place> getPlaceById(Long id);

    void deletePlace(long id);

    void addPlace(Integer parkingId, Place place);
}
