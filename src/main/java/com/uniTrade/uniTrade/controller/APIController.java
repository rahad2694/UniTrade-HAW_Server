package com.uniTrade.uniTrade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin(origins = "http://localhost:5173")
public class APIController {

    @GetMapping("/")
    public String renderApiInterface() {
        return "api_interface";
    }

}
