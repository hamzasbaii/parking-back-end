package com.parking.Parking.service;

import com.parking.Parking.Interfaces.IPlace;
import com.parking.Parking.entities.Parking;
import com.parking.Parking.entities.Place;
import com.parking.Parking.repository.ParkingRepository;
import com.parking.Parking.repository.PlaceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaceService implements IPlace {

    @Autowired
    ParkingRepository parkingRepo;

    @Autowired
    PlaceRepo placeRepo ;



    @Override
    public List<Place> getAllPLaces() {
        return placeRepo.findAll();
    }


    @Override
    public Optional<Place> getPlaceById(Long id) {
        return placeRepo.findById(id);
    }


    @Override
    public void deletePlace(long id) {
        placeRepo.deleteById(id);
    }

    @Override
    public void addPlace(Integer parkingId, Place place) {
        Parking parking = parkingRepo.findById(Long.valueOf(parkingId)).orElse(null);
        if (parking != null) {
            place.setParking(parking);

            placeRepo.save(place);

        }
    }
}
