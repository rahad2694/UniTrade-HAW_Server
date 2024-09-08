package com.uniTrade.uniTrade.controller;

import com.uniTrade.uniTrade.model.User;
import com.uniTrade.uniTrade.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = { "http://localhost:5173", "https://unitrade-haw-production.up.railway.app" })
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/allUsers")
    public ResponseEntity<List<User>> getAllUser() {
        List<User> users = userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        return userOptional.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/addUser")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User newUser = this.userRepository.save(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/update/{email}")
    public ResponseEntity<User> updateUserByMatriculation(@PathVariable String email, @RequestBody User user) {
        Optional<User> userOptional = this.userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User userToUpdate = userOptional.get();

            userToUpdate.setFirstName(user.getFirstName());
            userToUpdate.setLastName(user.getLastName());
            userToUpdate.setDob(user.getDob());
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setPassword(user.getPassword());
            userToUpdate.setRole(user.getRole());
            userToUpdate.setMatriculation(user.getMatriculation());
            userToUpdate.setLastUpdatedAt(LocalDateTime.now());
            userToUpdate.setAddress(user.getAddress());

            if (userToUpdate.getLastUpdatedAt() == null || userToUpdate.getLastUpdatedAt().isBefore(LocalDateTime.now())) {
                userToUpdate.setLastUpdatedAt(LocalDateTime.now());
            }

            return new ResponseEntity<>(this.userRepository.save(userToUpdate), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{userEmail}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userEmail) {
        Optional<User> userOptional = userRepository.findByEmail(userEmail);

        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/isAdmin/{email}")
    public boolean isAdmin(@PathVariable String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        System.out.println(userOptional);
        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            return existingUser.getRole().contains("ROLE_ADMIN");
        }
        return false;
    }
}
