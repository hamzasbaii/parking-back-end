package com.parking.Parking.service;

import com.parking.Parking.Interfaces.IParking;
import com.parking.Parking.entities.Parking;
import com.parking.Parking.entities.User;

import com.parking.Parking.repository.ParkingRepository;
import com.parking.Parking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParkingService implements IParking {


    @Autowired
    ParkingRepository parkingRepo;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<Parking> getAllParkings() {
        return parkingRepo.findAll();
    }

    @Override
    public Optional<Parking> getParkingById(Long id) {
        return parkingRepo.findById(id);
    }


    @Override
    public void deleteParking(long id) {
        parkingRepo.deleteById(id);
    }

    @Override
    public void addParking(Long userId, Parking parking) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            parking.setUser(user);

            parkingRepo.save(parking);

        }

    }
}