package com.parking.Parking.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    private String login;
    private String pwd;
    private String userName;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToMany
    @JoinTable(
            name = "user_parking",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "parking_id")
    )
    @JsonIgnoreProperties("managers")
    private Set<Parking> managedParkings;


    @ManyToMany
    @JoinTable(
            name = "user_vehicule",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "vehicule_id")
    )
    @JsonIgnoreProperties("owners")
    private Set<Vehicule> vehicules;

    // Method to affect a vehicle to the user
    public void affectVehicle(Vehicule vehicule) {
        vehicules.add(vehicule);
        vehicule.getOwners().add(this);
    }

    // Method to disuse a vehicle from the user
    public void disuseVehicle(Vehicule vehicule) {
        vehicules.remove(vehicule);
        vehicule.getOwners().remove(this);
    }

    public void affectParking(Parking parking) {
        managedParkings.add(parking);
        parking.getManagers().add(this);
    }
    public void disuseParking(Parking parking) {
        managedParkings.remove(parking);
        parking.getManagers().remove(this);
    }
}

