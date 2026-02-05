package com.backend.openai.controller;

import com.backend.openai.dto.GenericResponse;
import com.backend.openai.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class ApiController {

    @Autowired
    private AIService aiService;

    @GetMapping("/welcome")
    public GenericResponse<String> greet() {
        return aiService.greet();
    }

    @PostMapping("/chat")
    public GenericResponse<String> chat(@RequestBody String prompt) {
        return aiService.getResponseForPrompt(prompt);
    }
}
