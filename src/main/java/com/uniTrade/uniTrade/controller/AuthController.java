package com.uniTrade.uniTrade.controller;

import com.uniTrade.uniTrade.model.User;
import com.uniTrade.uniTrade.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn(@RequestBody String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        System.out.println("Email: " + email);

        System.out.println("Email: " + email);

        if (userOptional.isPresent()) {
            return new ResponseEntity<>("Sign-in successful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/sign-out")
    public ResponseEntity<String> signOut() {
        return new ResponseEntity<>("Sign-out successful", HttpStatus.OK);
    }
}