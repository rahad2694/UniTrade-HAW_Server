package com.uniTrade.uniTrade.service;

import com.uniTrade.uniTrade.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public User upsertUser(User user) {
        Query query = new Query(Criteria.where("email").is(user.getEmail()));

        Update update = new Update();

        // Check for all fields in the user model and only update if they are not null or empty
        if (user.getFirstName() != null) update.set("firstName", user.getFirstName());
        if (user.getLastName() != null) update.set("lastName", user.getLastName());
        if (user.getDob() != null) update.set("dob", user.getDob());
        if (user.getPassword() != null) update.set("password", user.getPassword());
        if (user.getRole() != null && !user.getRole().isEmpty()) update.set("role", user.getRole());
        if (user.getMatriculation() != 0) update.set("matriculation", user.getMatriculation());
        if (user.getAddress() != null) update.set("address", user.getAddress());
        if (user.getUserProfilePic() != null) update.set("userProfilePic", user.getUserProfilePic());

        // Always update the lastUpdatedAt field
        update.set("lastUpdatedAt", LocalDateTime.now());

        // Perform the upsert (update if exists, insert if not)
        mongoTemplate.upsert(query, update, User.class);

        // Return the updated user (fetch it again from the database)
        return mongoTemplate.findOne(query, User.class);
    }
}
