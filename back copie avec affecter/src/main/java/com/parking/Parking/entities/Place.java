package com.parking.Parking.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.parking.Parking.entities.Parking;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private Boolean dispo;
    private String position ;
    private Float prix ;



    @JsonIgnore
    @ManyToOne
    private Parking parking;

    @OneToOne(mappedBy = "place")
    private Reservation reservation;


}
