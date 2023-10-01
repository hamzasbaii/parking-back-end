package com.parking.Parking.service;

import com.parking.Parking.entities.Place;
import com.parking.Parking.entities.Reservation;
import com.parking.Parking.entities.Vehicule;
import com.parking.Parking.repository.ReservationRepo;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;




@Service
public class ReservationService {

    @Autowired
    ReservationRepo reservationRepo ;


    public Reservation createReservation(Vehicule vehicule, @NotNull Place place, Date dateEntrée, Date dateSortie) {
        if (place.getDispo()) {
            float prixTotale = calculatePrixTotale(place.getPrix(), dateEntrée, dateSortie);

            Reservation reservation = new Reservation();
            reservation.setVehicule(vehicule);
            reservation.setPlace(place);
            reservation.setDateEntrée(dateEntrée);
            reservation.setDateSortie(dateSortie);
            reservation.setPrixTotale(prixTotale);

            place.setDispo(false); // Marquer la place comme non disponible

            return reservationRepo.save(reservation);
        } else {
            // La place n'est pas disponible pour la réservation
            return null;
        }
    }

    // Méthode pour calculer le prix total de la réservation en fonction du temps passé dans le parking
    private float calculatePrixTotale(float prixPlace, Date dateEntrée, Date dateSortie) {
        // Mettez ici votre logique de calcul du prix total en fonction des dates d'entrée et de sortie
        // Par exemple, vous pouvez utiliser la différence entre les dates pour calculer le temps passé dans le parking et appliquer un tarif horaire.
        // Pour simplifier l'exemple, je vais simplement multiplier le prix de la place par le nombre de jours passés.
        long diff = dateSortie.getTime() - dateEntrée.getTime();
        int days = (int) (diff / (1000 * 60 * 60 * 24));
        return prixPlace * days;
    }


}
