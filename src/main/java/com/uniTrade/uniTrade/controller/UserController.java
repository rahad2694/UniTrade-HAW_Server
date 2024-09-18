package com.uniTrade.uniTrade.controller;

import com.uniTrade.uniTrade.model.User;
import com.uniTrade.uniTrade.repository.UserRepository;
import com.uniTrade.uniTrade.service.UserService;
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
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

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
        Optional<User> existingUserOptional = userRepository.findByEmail(user.getEmail());

        if (existingUserOptional.isPresent()) {
            // If the user exists, return a 409 Conflict status
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            // If the user does not exist, create the new user
            User newUser = userService.upsertUser(user);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody String email) {
        if(userRepository.findByEmail(email).isPresent()) {
            return new ResponseEntity<>(userRepository.findByEmail(email).get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        // Call the upsert method in UserService
        User updatedUser = userService.upsertUser(user);

        if (updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
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
