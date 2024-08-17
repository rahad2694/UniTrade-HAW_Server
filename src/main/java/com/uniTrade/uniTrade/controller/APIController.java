package com.uniTrade.uniTrade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class APIController {

    @GetMapping("/")
    public String renderApiInterface() {
        return "api_interface";
    }

}
