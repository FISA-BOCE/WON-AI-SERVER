package com.woorifisa.won_ai_server.domain.chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Map;

public record AnswerRequest(
        @NotBlank String originalMessage,
        @NotBlank
        @Pattern(regexp = "CARD_MONTHLY_TOTAL_SPEND|POINT_CURRENT_BALANCE|POINT_MONTHLY_EARNED|ETF_LIST|ETF_AMOUNT|SAME_ETF_AVERAGE_POINT|MY_POINT_INVESTMENT_PATH|UNKNOWN")
        String queryType,
        @NotNull Map<String, Object> dbResult
) {}