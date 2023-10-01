package com.parking.Parking.controller;

import com.parking.Parking.entities.Parking;
import com.parking.Parking.entities.User;
import com.parking.Parking.repository.ParkingRepository;
import com.parking.Parking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/")
public class ParkingController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ParkingRepository parkingRepository;

    @GetMapping("/parkings")
    public List<Parking> getAllParkings() {
        List<Parking> parkings = parkingRepository.findAll();
        return parkings;
    }

    @GetMapping("/parkings/{id}")
    public ResponseEntity<Parking> getParkingById(@PathVariable Long id) {
        Parking parking = parkingRepository.findById(id).orElseThrow(() -> new ParkingController.ParkingNotFoundException(id));
        return ResponseEntity.ok(parking);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class ParkingNotFoundException extends RuntimeException {
        public ParkingNotFoundException(Long id) {
            super("Parking not found with ID: " + id);
        }
    }

    @PostMapping("/parkings")
    @ResponseStatus(HttpStatus.CREATED)
    public Parking createParking(@RequestBody Parking parking) {
        Parking savedParking = parkingRepository.save(parking);
        return savedParking;
    }

    @PutMapping("/parkings/{id}")
    public ResponseEntity<Parking> updateParking(@PathVariable Long id, @RequestBody Parking parkingDetails) {

        Parking parking = parkingRepository.findById(id).orElseThrow(() -> new ParkingController.ParkingNotFoundException(id));

        parking.setFull(parkingDetails.getFull());
        parking.setPosition(parkingDetails.getPosition());

        Parking updatedParking = parkingRepository.save(parking);

        return ResponseEntity.ok(updatedParking);
    }

    @DeleteMapping("/parkings/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteParking(@PathVariable Long id) {
        Parking parking = parkingRepository.findById(id).orElseThrow(() -> new ParkingController.ParkingNotFoundException(id));

        for (User manager : parking.getManagers()) {
            manager.getManagedParkings().remove(parking);
        }
        parking.getManagers().clear();
        parkingRepository.delete(parking);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/check-position")
    public ResponseEntity<Void> checkPositionAvailability(@RequestBody String position, @RequestParam(required = false) Long parkingId) {
        if (parkingId != null) {
            // If parkingId is provided, exclude it from the position availability check
            boolean positionExists = parkingRepository.existsByPositionAndIdNot(position, parkingId);

            if (positionExists) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Return an error response
            } else {
                return ResponseEntity.ok().build(); // Return a success response
            }
        } else {
            // If parkingId is not provided, perform the position availability check for create and signup
            boolean positionExists = parkingRepository.existsByPosition(position);

            if (positionExists) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Return an error response
            } else {
                return ResponseEntity.ok().build(); // Return a success response
            }
        }
    }

    @PostMapping("/users/{userId}/affect-parking")
    public ResponseEntity<Void> affectParkingToUser(@PathVariable Long userId, @RequestBody Map<String, String> request) {
        String position = request.get("position");
        User user = userRepository.findById(userId).orElseThrow(() -> new UserController.UserNotFoundException(userId));
        Parking parking = parkingRepository.findByPosition(position);

        if (parking == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Parking not found
        }

        // Check if the parking is not already affected to the user
        if (!user.getManagedParkings().contains(parking)) {
            user.affectParking(parking);
            userRepository.save(user);
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{userId}/check-parking-affected/{parkingId}")
    public ResponseEntity<Boolean> checkParkingAffectedByUser(@PathVariable Long userId, @PathVariable Long parkingId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserController.UserNotFoundException(userId));
        Parking parking = parkingRepository.findById(parkingId).orElseThrow(() -> new ParkingController.ParkingNotFoundException(parkingId));

        // Check if the vehicle is affected to the user
        boolean isAffected = user.getManagedParkings().contains(parking);

        return ResponseEntity.ok(isAffected);
    }

    @DeleteMapping("/users/{userId}/parkings/{parkingId}")
    public ResponseEntity<Map<String, Boolean>> removeParkingFromUser(@PathVariable Long userId, @PathVariable Long parkingId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserController.UserNotFoundException(userId));
        Parking parking = parkingRepository.findById(parkingId).orElseThrow(() -> new ParkingController.ParkingNotFoundException(parkingId));

        // Check if the parking is affected to the user
        if (user.getManagedParkings().contains(parking)) {
            user.getManagedParkings().remove(parking);
            userRepository.save(user);
        }

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

}
