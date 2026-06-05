package com.woorifisa.won_ai_server.domain.chat.service;

import com.woorifisa.won_ai_server.domain.chat.dto.request.ClassifyRequest;
import com.woorifisa.won_ai_server.domain.chat.dto.response.ClassifyResponse;

public interface ClassifyService {
    ClassifyResponse classify(ClassifyRequest request);
}