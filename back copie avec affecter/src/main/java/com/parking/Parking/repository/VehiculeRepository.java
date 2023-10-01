package com.parking.Parking.repository;

import com.parking.Parking.entities.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {
    boolean existsByMatricule(String matricule);
    boolean existsByMatriculeAndIdNot(String matricule, Long vehiculeId);

    Vehicule findByMatricule(String matricule);
}
