package com.backend.openai.service;

import com.backend.openai.dto.GenericResponse;

public interface AIService {
    GenericResponse<String> greet();
    GenericResponse<String> getResponseForPrompt(String prompt);
}
