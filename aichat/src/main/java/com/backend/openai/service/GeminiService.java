package com.backend.openai.service;

import com.backend.openai.dto.GenericResponse;
import com.backend.openai.util.Constants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;

@Service("GEMINI")
public class GeminiService implements AIService {

    private static final MediaType JSON =
            MediaType.parse("application/json");

    private static final String API_BASE =
            "https://generativelanguage.googleapis.com/v1";

    private static final String MODEL_NAME =
            "models/gemini-1.5-flash";

    private final String apiKey;
    private final OkHttpClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public GeminiService(
            @Value("${gemini.api.key}") String apiKey
    ) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Gemini API key is missing");
        }

        this.apiKey = apiKey;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(10))
                .readTimeout(Duration.ofSeconds(30))
                .callTimeout(Duration.ofSeconds(30))
                .build();
    }

    @Override
    public GenericResponse<String> getResponseForPrompt(String prompt) {
        long startTime = System.currentTimeMillis();

        String url = API_BASE + "/" + MODEL_NAME + ":generateContent?key=" + apiKey;

        try {
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(
                            mapper.writeValueAsString(buildRequest(prompt)),
                            JSON
                    ))
                    .addHeader("Content-Type", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    String errorBody = response.body() != null
                            ? response.body().string()
                            : "<empty>";

                    throw new IOException(
                            "Gemini API error " + response.code() + ": " + errorBody
                    );
                }

                JsonNode root = mapper.readTree(response.body().string());
                String text = extractText(root);

                return new GenericResponse<>(
                        Constants.RAM_200,
                        Constants.SUCCESS,
                        stripMarkdownCodeBlock(text),
                        System.currentTimeMillis() - startTime
                );
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to call Gemini API", e);
        }
    }

    @Override
    public GenericResponse<String> greet() {
        return new GenericResponse<>(
                Constants.RAM_200,
                Constants.SUCCESS,
                "Gemini service is ready",
                0
        );
    }

    /* -------------------------- helpers -------------------------- */

    private ObjectNode buildRequest(String prompt) {
        ObjectNode textNode = mapper.createObjectNode();
        textNode.put("text", prompt);

        ObjectNode partsNode = mapper.createObjectNode();
        partsNode.set("parts", mapper.createArrayNode().add(textNode));

        ObjectNode root = mapper.createObjectNode();
        root.set("contents", mapper.createArrayNode().add(partsNode));

        // Optional but recommended: deterministic output
        root.set("generationConfig", mapper.createObjectNode()
                .put("temperature", 0.2)
                .put("maxOutputTokens", 512));

        return root;
    }

    private String extractText(JsonNode root) {
        return root
                .path("candidates")
                .path(0)
                .path("content")
                .path("parts")
                .path(0)
                .path("text")
                .asText("");
    }

    private String stripMarkdownCodeBlock(String text) {
        if (text != null && text.startsWith("```")) {
            int firstNewline = text.indexOf('\n');
            int lastTripleBacktick = text.lastIndexOf("```");
            if (firstNewline > 0 && lastTripleBacktick > firstNewline) {
                return text.substring(firstNewline + 1, lastTripleBacktick).trim();
            }
        }
        return text;
    }
}
