package com.uniTrade.uniTrade.controller;

import com.uniTrade.uniTrade.model.Lead;
import com.uniTrade.uniTrade.model.User;
import com.uniTrade.uniTrade.repository.LeadRepository;
import com.uniTrade.uniTrade.repository.UserRepository;
import com.uniTrade.uniTrade.service.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/leads")
@CrossOrigin(origins = { "http://localhost:5173", "https://unitrade-haw-production.up.railway.app" })
public class LeadController {

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeadService leadService;

    @GetMapping("/leads")
    public ResponseEntity<List<Lead>> getAllLeads() {
        List<Lead> leads = leadRepository.findAllByOrderByCreatedAtDesc();
        return new ResponseEntity<>(leads, HttpStatus.OK);
    }

    @GetMapping("/lead-by-userEmail/{userEmail}")
    public ResponseEntity<List<Lead>> getLeadsByUserEmail(@PathVariable String userEmail) {
        List<Lead> leads = leadRepository.findByUserEmail(userEmail);

        System.out.println("User: " + userEmail);
        System.out.println("Leads found: " + leads.size());

        if (leads.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(leads, HttpStatus.OK);
        }
    }


    @GetMapping("/lead-by-id/{id}")
    public ResponseEntity<Lead> getLeadByLeadId(@PathVariable String id) {
        Optional<Lead> leadOptional = leadRepository.findById(id);

        return leadOptional.map(lead -> new ResponseEntity<>(lead, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create-lead")
    public ResponseEntity<Lead> createLead(@RequestBody Lead lead) {
        Optional<User> userOptional = userRepository.findByEmail(lead.getUserEmail());

        // Ensure the lead has a valid user
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        System.out.println("Creating lead");
        System.out.println("Lead: " + lead);
        Lead newLead = leadRepository.save(lead);
        return new ResponseEntity<>(newLead, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Lead> updateLead(@PathVariable String id, @RequestBody Lead lead) {
        // Check if the lead exists in the repository
        Optional<Lead> leadOptional = leadRepository.findById(id);

        if (leadOptional.isPresent()) {
            Lead leadToUpdate = leadOptional.get();

            // Verify if the userEmail matches with the one in the database
            if (!leadToUpdate.getUserEmail().equals(lead.getUserEmail())) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);  // Return unauthorized if emails don't match
            }
            // Use the upsert method from LeadService to update the lead
            Lead updatedLead = leadService.upsertLead(id, lead);
            return new ResponseEntity<>(updatedLead, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // If the lead doesn't exist, return not found
        }
    }

    @DeleteMapping("/delete/{userEmail}/{id}")
    public ResponseEntity<Void> deleteLead(@PathVariable String userEmail, @PathVariable String id) {
        Optional<Lead> leadOptional = leadRepository.findById(id);

        if (leadOptional.isPresent()) {
            Lead lead = leadOptional.get();
            if (lead.getUserEmail().equals(userEmail)) {
                leadRepository.deleteById(lead.getId());
                System.out.println(lead.toString());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
