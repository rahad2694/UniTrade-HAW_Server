package com.uniTrade.uniTrade.service;

import com.uniTrade.uniTrade.model.Lead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LeadService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Lead upsertLead(String id, Lead lead) {
        Query query = new Query(Criteria.where("id").is(id));

        Update update = new Update();

        // Update fields only if they are not null or empty
        if (lead.getUserEmail() != null) update.set("userEmail", lead.getUserEmail());
        if (lead.getContent() != null) update.set("content", lead.getContent());
        if (lead.getLeadTitle() != null) update.set("leadTitle", lead.getLeadTitle());
        if (lead.getImageUrls() != null && !lead.getImageUrls().isEmpty()) update.set("imageUrls", lead.getImageUrls());
        if (lead.getComments() != null && !lead.getComments().isEmpty()) update.set("comments", lead.getComments());
        if (lead.getLikes() != null && !lead.getLikes().isEmpty()) update.set("likes", lead.getLikes());

        // Always update the lastUpdatedAt field
        update.set("lastUpdatedAt", LocalDateTime.now());

        // Perform the upsert
        mongoTemplate.upsert(query, update, Lead.class);

        // Return the updated lead
        return mongoTemplate.findOne(query, Lead.class);
    }

    public Lead addLikeToLead(String leadId, String userEmail) {
        Query query = new Query(Criteria.where("id").is(leadId));
        Update update = new Update().addToSet("likes", userEmail);
        update.set("lastUpdatedAt", LocalDateTime.now());

        // Update the lead and return the result
        return mongoTemplate.findAndModify(query, update, Lead.class);
    }

}
