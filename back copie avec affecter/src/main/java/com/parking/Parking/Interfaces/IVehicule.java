package com.parking.Parking.Interfaces;

import com.parking.Parking.entities.Vehicule;

import java.util.Optional;

public interface IVehicule {
    Optional<Vehicule> getVehiculeById(Long id);
}
