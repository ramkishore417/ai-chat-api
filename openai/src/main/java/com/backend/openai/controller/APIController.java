package com.backend.openai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/openai")
public class APIController {
    
    @GetMapping("/welcome")
    public String greet() {
        return "Welcome";
    }

}
