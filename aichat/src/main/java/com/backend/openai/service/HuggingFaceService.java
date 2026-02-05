package com.backend.openai.service;

import com.backend.openai.dto.GenericResponse;
import com.backend.openai.util.Constants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;

@Service("HUGGING_FACE")
@Primary
public class HuggingFaceService implements AIService {

    private static final MediaType JSON =
            MediaType.parse("application/json");

    private static final String CHAT_COMPLETIONS_URL =
            "https://router.huggingface.co/v1/chat/completions";

    private static final String DEFAULT_SYSTEM_PROMPT =
            "You are a helpful assistant.";

    private static final int MAX_TOKENS = 256;
    private static final double TEMPERATURE = 0.2;

    private final String apiKey;
    private final String model;
    private final OkHttpClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public HuggingFaceService(
            @Value("${huggingface.api.key}") String apiKey,
            @Value("${huggingface.model}") String model
    ) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Hugging Face API key is missing");
        }
        if (model == null || model.isBlank()) {
            throw new IllegalStateException("Hugging Face model is missing");
        }

        this.apiKey = apiKey;
        this.model = model;

        this.client = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(10))
                .readTimeout(Duration.ofSeconds(90))   // HF cold starts
                .callTimeout(Duration.ofSeconds(90))
                .build();
    }

    @Override
    public GenericResponse<String> getResponseForPrompt(String prompt) {
        return getResponseForPrompt(DEFAULT_SYSTEM_PROMPT, prompt);
    }

    public GenericResponse<String> getResponseForPrompt(
            String systemPrompt,
            String userPrompt
    ) {
        long startTime = System.currentTimeMillis();

        try {
            ObjectNode requestBody = buildRequest(systemPrompt, userPrompt);

            Request request = new Request.Builder()
                    .url(CHAT_COMPLETIONS_URL)
                    .post(RequestBody.create(
                            mapper.writeValueAsString(requestBody),
                            JSON
                    ))
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {

                // HF free-tier cold start
                if (response.code() == 503) {
                    throw new IOException(
                            "Hugging Face model is loading (cold start). Retry shortly."
                    );
                }

                if (!response.isSuccessful()) {
                    String errorBody = response.body() != null
                            ? response.body().string()
                            : "<empty>";

                    throw new IOException(
                            "Hugging Face API error " + response.code() + ": " + errorBody
                    );
                }

                JsonNode root = mapper.readTree(response.body().string());
                String content = extractAssistantText(root);

                return new GenericResponse<>(
                        Constants.RAM_200,
                        Constants.SUCCESS,
                        content,
                        System.currentTimeMillis() - startTime
                );
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to call Hugging Face chat API", e);
        }
    }

    @Override
    public GenericResponse<String> greet() {
        return new GenericResponse<>(
                Constants.RAM_200,
                Constants.SUCCESS,
                "Hugging Face chat service is ready",
                0
        );
    }

    /* --------------------------- helpers --------------------------- */

    private ObjectNode buildRequest(String systemPrompt, String userPrompt) {
        ObjectNode root = mapper.createObjectNode();
        root.put("model", model);
        root.put("max_tokens", MAX_TOKENS);
        root.put("temperature", TEMPERATURE);

        ArrayNode messages = mapper.createArrayNode();
        messages.add(message("system", systemPrompt));
        messages.add(message("user", userPrompt));

        root.set("messages", messages);
        return root;
    }

    private ObjectNode message(String role, String content) {
        ObjectNode node = mapper.createObjectNode();
        node.put("role", role);
        node.put("content", content);
        return node;
    }

    /**
     * Safely extracts:
     * choices[0].message.content
     */
    private String extractAssistantText(JsonNode root) {
        if (root == null) {
            return "";
        }

        JsonNode choices = root.path("choices");
        if (!choices.isArray() || choices.isEmpty()) {
            return "";
        }

        JsonNode message = choices.get(0).path("message");
        if (message.isMissingNode()) {
            return "";
        }

        return message.path("content").asText("").trim();
    }
}
