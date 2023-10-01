package com.parking.Parking.service;

import com.parking.Parking.Interfaces.IVehicule;
import com.parking.Parking.entities.Vehicule;
import com.parking.Parking.repository.VehiculeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VehiculeService implements IVehicule {


 @Autowired
    VehiculeRepository vehiculeRepo;

    @Override
    public Optional <Vehicule> getVehiculeById(Long id) {
        return vehiculeRepo.findById(id);
    }



}
