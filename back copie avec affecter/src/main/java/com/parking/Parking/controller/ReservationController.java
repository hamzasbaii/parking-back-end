package com.parking.Parking.controller;


import com.parking.Parking.entities.Place;
import com.parking.Parking.entities.Reservation;
import com.parking.Parking.entities.Vehicule;


import com.parking.Parking.repository.PlaceRepo;
import com.parking.Parking.repository.VehiculeRepository;
import com.parking.Parking.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping("/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {


    @Autowired
    ReservationService reservationService;

    @Autowired
    VehiculeRepository vehiculeRepository;

    @Autowired
    PlaceRepo placeRepo;




    @PostMapping("/create")
    public Reservation createReservation(@RequestParam Long vehiculeId,
                                         @RequestParam Long placeId,
                                         @RequestParam Date dateEntrée,
                                         @RequestParam Date dateSortie) {

        // Ici, vous devrez implémenter la logique pour récupérer le véhicule et la place à partir de leur ID (vous pouvez utiliser le service approprié pour cela)
        // En supposant que vous ayez les entités Vehicule et Place dans le service correspondant

        Vehicule vehicule = vehiculeRepository.getById(vehiculeId);
        Place place = placeRepo.getById(placeId);// Récupérer la place par son ID

        if (vehicule != null && place != null) {
            return reservationService.createReservation(vehicule, place, dateEntrée, dateSortie);
        } else {
            // Vérifier si le véhicule et la place existent
            return null;
        }
    }



}
