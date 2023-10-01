package com.parking.Parking.controller;

import com.parking.Parking.entities.Vehicule;
import com.parking.Parking.entities.User;

import com.parking.Parking.repository.VehiculeRepository;
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
public class VehiculeController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VehiculeRepository vehiculeRepository;

    @GetMapping("/vehicules")
    public List<Vehicule> getAllVehicules() {
        List<Vehicule> vehicule = vehiculeRepository.findAll();
        return vehicule;
    }

    @GetMapping("/vehicules/{id}")
    public ResponseEntity<Vehicule> getVehiculeById(@PathVariable Long id) {
        Vehicule vehicule = vehiculeRepository.findById(id).orElseThrow(() -> new VehiculeController.VehiculeNotFoundException(id));
        return ResponseEntity.ok(vehicule);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class VehiculeNotFoundException extends RuntimeException {
        public VehiculeNotFoundException(Long id) {
            super("Vehicule not found with ID: " + id);
        }
    }

    @PostMapping("/vehicules")
    @ResponseStatus(HttpStatus.CREATED)
    public Vehicule createVehicule(@RequestBody Vehicule vehicule) {
        Vehicule savedVehicule = vehiculeRepository.save(vehicule);
        return savedVehicule;
    }

    @PutMapping("/vehicules/{id}")
    public ResponseEntity<Vehicule> updateVehicule(@PathVariable Long id, @RequestBody Vehicule vehiculeDetails) {

        Vehicule vehicule = vehiculeRepository.findById(id).orElseThrow(() -> new VehiculeController.VehiculeNotFoundException(id));

        vehicule.setType(vehiculeDetails.getType());
        vehicule.setMatricule(vehiculeDetails.getMatricule());

        Vehicule updatedVehicule = vehiculeRepository.save(vehicule);

        return ResponseEntity.ok(updatedVehicule);
    }

    @DeleteMapping("/vehicules/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteVehicule(@PathVariable Long id) {
        Vehicule vehicule = vehiculeRepository.findById(id).orElseThrow(() -> new VehiculeController.VehiculeNotFoundException(id));

        for (User manager : vehicule.getOwners()) {
            manager.getVehicules().remove(vehicule);
        }
        vehicule.getOwners().clear();
        vehiculeRepository.delete(vehicule);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/check-matricule")
    public ResponseEntity<Void> checkMatriculeAvailability(@RequestBody String matricule, @RequestParam(required = false) Long vehiculeId) {
        if (vehiculeId != null) {
            // If vehiculeId is provided, exclude it from the matricule availability check
            boolean matriculeExists = vehiculeRepository.existsByMatriculeAndIdNot(matricule, vehiculeId);

            if (matriculeExists) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Return an error response
            } else {
                return ResponseEntity.ok().build(); // Return a success response
            }
        } else {
            // If vehiculeId is not provided, perform the matricule availability check for create and signup
            boolean matriculeExists = vehiculeRepository.existsByMatricule(matricule);

            if (matriculeExists) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Return an error response
            } else {
                return ResponseEntity.ok().build(); // Return a success response
            }
        }
    }

    /*@PostMapping("/users/{userId}/affect-vehicle/{vehicleId}")
    public ResponseEntity<Void> affectVehicleToUser(@PathVariable Long userId, @PathVariable Long vehicleId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserController.UserNotFoundException(userId));
        Vehicule vehicule = vehiculeRepository.findById(vehicleId).orElseThrow(() -> new VehiculeNotFoundException(vehicleId));

        // Check if the vehicle is not already affected to the user
        if (!user.getVehicules().contains(vehicule)) {
            user.getVehicules().add(vehicule);
            userRepository.save(user);
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/vehicules/check-matricule")
    public ResponseEntity<Boolean> checkMatriculeExists(@RequestBody String matricule) {
        boolean matriculeExists = vehiculeRepository.existsByMatricule(matricule);
        return ResponseEntity.ok(matriculeExists);
    }*/

    @PostMapping("/users/{userId}/affect-vehicle")
    public ResponseEntity<Void> affectVehicleToUser(@PathVariable Long userId, @RequestBody Map<String, String> request) {
        String matricule = request.get("matricule");
        User user = userRepository.findById(userId).orElseThrow(() -> new UserController.UserNotFoundException(userId));
        Vehicule vehicule = vehiculeRepository.findByMatricule(matricule);

        if (vehicule == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Vehicle not found
        }

        // Check if the vehicle is not already affected to the user
        if (!user.getVehicules().contains(vehicule)) {
            user.affectVehicle(vehicule);
            userRepository.save(user);
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{userId}/check-vehicle-affected/{vehicleId}")
    public ResponseEntity<Boolean> checkVehicleAffectedByUser(@PathVariable Long userId, @PathVariable Long vehicleId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserController.UserNotFoundException(userId));
        Vehicule vehicule = vehiculeRepository.findById(vehicleId).orElseThrow(() -> new VehiculeNotFoundException(vehicleId));

        // Check if the vehicle is affected to the user
        boolean isAffected = user.getVehicules().contains(vehicule);

        return ResponseEntity.ok(isAffected);
    }

    @DeleteMapping("/users/{userId}/vehicules/{vehicleId}")
    public ResponseEntity<Map<String, Boolean>> removeVehicleFromUser(@PathVariable Long userId, @PathVariable Long vehicleId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserController.UserNotFoundException(userId));
        Vehicule vehicule = vehiculeRepository.findById(vehicleId).orElseThrow(() -> new VehiculeController.VehiculeNotFoundException(vehicleId));

        // Check if the vehicle is affected to the user
        if (user.getVehicules().contains(vehicule)) {
            user.getVehicules().remove(vehicule);
            userRepository.save(user);
        }

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }



}
