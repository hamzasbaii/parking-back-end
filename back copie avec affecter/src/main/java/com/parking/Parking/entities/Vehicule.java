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
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String matricule;
    private String type;

    @OneToOne(mappedBy = "vehicule")
    private Reservation reservation;

    @ManyToMany(mappedBy = "vehicules")
    @JsonIgnoreProperties("vehicules")
    private Set<User> owners;
}
