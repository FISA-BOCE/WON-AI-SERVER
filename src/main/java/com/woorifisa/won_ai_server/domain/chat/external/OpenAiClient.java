package com.woorifisa.won_ai_server.domain.chat.external;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OpenAiClient {

    private final WebClient openAiWebClient;

    @Value("${azure-openai.model}")
    private String model;

    @Value("${azure-openai.api-version}")
    private String apiVersion;

    public String callWithJsonResponse(String systemPrompt, String userMessage) {
        Map<String, Object> requestBody = Map.of(
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userMessage)
                ),
                "response_format", Map.of("type", "json_object")
        );
        return extractContent(requestBody);
    }

    public String callWithTextResponse(String systemPrompt, String userMessage) {
        Map<String, Object> requestBody = Map.of(
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userMessage)
                )
        );
        return extractContent(requestBody);
    }

    private String extractContent(Map<String, Object> requestBody) {
        String uri = String.format("/openai/deployments/%s/chat/completions?api-version=%s", model, apiVersion);
        JsonNode response = openAiWebClient.post()
                .uri(uri)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
        return response.path("choices").get(0).path("message").path("content").asText();
    }
}