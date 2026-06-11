package com.woorifisa.won_ai_server.domain.chat.dto.response;

import java.util.Map;

public record ClassifyResponse(
        QueryType queryType,
        DbTarget dbTarget,
        DataSource dataSource,
        double confidence,
        Map<String, String> params
) {}
