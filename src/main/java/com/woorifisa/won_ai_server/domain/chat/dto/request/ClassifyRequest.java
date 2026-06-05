package com.woorifisa.won_ai_server.domain.chat.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ClassifyRequest(@NotBlank String message) {}