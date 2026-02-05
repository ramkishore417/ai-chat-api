package com.backend.openai.service;

import com.backend.openai.dto.GenericResponse;
import com.backend.openai.util.Constants;
import org.springframework.stereotype.Service;

@Service("OPENAI")
public class OpenAIService implements AIService {
    @Override
    public GenericResponse<String> greet() {
        return new GenericResponse<>(
                Constants.RAM_200,
                Constants.SUCCESS,
                "OpenAI service is not ready",
                0
        );
    }

    @Override
    public GenericResponse<String> getResponseForPrompt(String prompt) {
        return new GenericResponse<>(
                Constants.RAM_200,
                Constants.SUCCESS,
                "OpenAI service is not ready",
                0
        );
    }
}
