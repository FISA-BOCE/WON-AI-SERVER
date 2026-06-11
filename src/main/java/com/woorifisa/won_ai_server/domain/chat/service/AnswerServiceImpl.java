package com.woorifisa.won_ai_server.domain.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woorifisa.won_ai_server.domain.chat.dto.request.AnswerRequest;
import com.woorifisa.won_ai_server.domain.chat.dto.response.AnswerResponse;
import com.woorifisa.won_ai_server.domain.chat.external.OpenAiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private static final String SYSTEM_PROMPT = """
            너는 금융 챗봇이야.
            사용자의 질문과 DB 조회 결과를 바탕으로 친절하고 자연스러운 한국어 답변을 생성해.
            숫자는 정확하게 포함하고, 불필요한 부연 설명은 생략해.
            """;

    private final OpenAiClient openAiClient;
    private final ObjectMapper objectMapper;

    @Override
    public AnswerResponse generateAnswer(AnswerRequest request) {
        String userMessage = String.format(
                "질문: %s\n조회 유형: %s\nDB 조회 결과: %s",
                request.originalMessage(),
                request.queryType(),
                serializeDbResult(request)
        );
        String answer = openAiClient.callWithTextResponse(SYSTEM_PROMPT, userMessage);
        return new AnswerResponse(answer);
    }

    private String serializeDbResult(AnswerRequest request) {
        try {
            return objectMapper.writeValueAsString(request.dbResult());
        } catch (JsonProcessingException e) {
            return request.dbResult().toString();
        }
    }
}