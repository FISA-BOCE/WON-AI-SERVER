package com.woorifisa.won_ai_server.domain.chat.service;

import com.woorifisa.won_ai_server.domain.chat.dto.request.AnswerRequest;
import com.woorifisa.won_ai_server.domain.chat.dto.response.AnswerResponse;

public interface AnswerService {
    AnswerResponse generateAnswer(AnswerRequest request);
}