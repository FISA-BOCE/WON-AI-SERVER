package com.woorifisa.won_ai_server.domain.chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record AnswerRequest(
        @NotBlank String originalMessage,
        @NotBlank String queryType,
        @NotNull Map<String, Object> dbResult
) {}