package com.parking.Parking.controller;

import com.parking.Parking.Interfaces.IPlace;
import com.parking.Parking.entities.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Place")
public class PlaceController {

     @Autowired
    IPlace placeS;

    @GetMapping("/all")
    public ResponseEntity<List<Place>> getAllPlaces() {
        List<Place> places = placeS.getAllPLaces();
        return new ResponseEntity<List<Place>>(places, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Place> getPlaceById(@PathVariable Long id) {
        Optional<Place> places = placeS.getPlaceById(id);
        return new ResponseEntity<>(places.orElse(null), HttpStatus.OK);
    }

    @PostMapping("/place/{parkingId}")
    public ResponseEntity<String> ajouterPlace(@PathVariable Integer parkingId, @RequestBody Place place) {
        placeS.addPlace(parkingId, place);
        return new ResponseEntity<>("La Place a été ajoutée avec succès.", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public void deletePlace(@PathVariable Long id) {
        placeS.deletePlace(id);
    }



}
