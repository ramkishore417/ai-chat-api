package com.backend.openai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import com.backend.openai.dto.GenericResponse;
import com.backend.openai.service.APIService;

@RestController
@RequestMapping("/openai")
public class APIController {

    private final APIService apiService;

    @Autowired
    public APIController(APIService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/welcome")
    public GenericResponse<String> greet() {
        return apiService.greet();
    }

}
