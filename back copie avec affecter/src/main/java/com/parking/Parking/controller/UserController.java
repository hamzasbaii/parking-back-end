package com.parking.Parking.controller;

import com.parking.Parking.entities.Parking;
import com.parking.Parking.entities.Role;
import com.parking.Parking.entities.User;
import com.parking.Parking.entities.Vehicule;
import com.parking.Parking.repository.ParkingRepository;
import com.parking.Parking.repository.RoleRepository;
import com.parking.Parking.repository.UserRepository;
import com.parking.Parking.repository.VehiculeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return ResponseEntity.ok(user);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(Long id) {
            super("User not found with ID: " + id);
        }
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user) {
        // Retrieve the role from the database using the role_id
        Role role = roleRepository.findById(user.getRole().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid role_id"));

        // Assign the retrieved role to the user
        user.setRole(role);

        // Save the user entity to the database
        User savedUser = userRepository.save(user);

        // Return the saved user entity
        return savedUser;
    }

    @GetMapping("users/roles")
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // adding an existing vehicle to a user
    @PostMapping("/users/{userId}/vehicules/{vehiculeId}")
    public ResponseEntity<String> addExistingVehiculeForUser(@PathVariable Long userId, @PathVariable Long vehiculeId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Vehicule> optionalVehicule = vehiculeRepository.findById(vehiculeId);

        if (optionalUser.isPresent() && optionalVehicule.isPresent()) {
            User user = optionalUser.get();
            Vehicule vehicule = optionalVehicule.get();

            user.getVehicules().add(vehicule);
            vehicule.getOwners().add(user);

            userRepository.save(user);
            return ResponseEntity.ok("Existing vehicule added for the user.");
        }

        return ResponseEntity.notFound().build();
    }

    // adding an existing parking to a user
    @PostMapping("/users/{userId}/parkings/{parkingId}")
    public ResponseEntity<String> addExistingParkingForUser(@PathVariable Long userId, @PathVariable Long parkingId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Parking> optionalParking = parkingRepository.findById(parkingId);

        if (optionalUser.isPresent() && optionalParking.isPresent()) {
            User user = optionalUser.get();
            Parking parking = optionalParking.get();

            user.getManagedParkings().add(parking);
            parking.getManagers().add(user);

            userRepository.save(user);
            return ResponseEntity.ok("Existing parking added for the user.");
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        user.setUserName(userDetails.getUserName());
        user.setLogin(userDetails.getLogin());
        user.setPwd(userDetails.getPwd());

        // Retrieve the role from the database using the role_id
        Role role = roleRepository.findById(userDetails.getRole().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid role_id"));

        // Assign the retrieved role to the user
        user.setRole(role);

        User updatedUser = userRepository.save(user);

        return ResponseEntity.ok(updatedUser);
    }

    // delete user by Id

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setRole(null); // Disassociate the user from the role
        user.getManagedParkings().clear(); // Disassociate the user from the parkings
        user.getVehicules().clear(); // disassociate the user from the vehicules
        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody Map<String, String> credentials) {
        String login = credentials.get("login");
        String pwd = credentials.get("pwd");

        User existingUser = userRepository.findByLogin(login);
        if (existingUser == null || !existingUser.getPwd().equals(pwd)) {
            // User not found or password doesn't match
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Return the authenticated user
        return ResponseEntity.ok(existingUser);
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signupUser(@RequestBody User user) {
        // Retrieve the role from the database using the role_id
        Role role = roleRepository.findById(user.getRole().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid role_id"));

        // Assign the retrieved role to the user
        user.setRole(role);

        // Save the user entity to the database
        User savedUser = userRepository.save(user);

        // Return the saved user entity
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PostMapping("/check-login")
    public ResponseEntity<Void> checkLoginAvailability(@RequestBody String login, @RequestParam(required = false) Long userId) {
        if (userId != null) {
            // If userId is provided, exclude it from the login availability check
            boolean loginExists = userRepository.existsByLoginAndIdNot(login, userId);

            if (loginExists) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Return an error response
            } else {
                return ResponseEntity.ok().build(); // Return a success response
            }
        } else {
            // If userId is not provided, perform the login availability check for create and signup
            boolean loginExists = userRepository.existsByLogin(login);

            if (loginExists) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Return an error response
            } else {
                return ResponseEntity.ok().build(); // Return a success response
            }
        }
    }

    @PostMapping("/users/{userId}/affect-vehicle/{vehicleId}")
    public ResponseEntity<Void> affectVehicleToUser(@PathVariable Long userId, @PathVariable Long vehicleId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Vehicule vehicule = vehiculeRepository.findById(vehicleId).orElseThrow(() -> new VehiculeController.VehiculeNotFoundException(vehicleId));

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
    }

}