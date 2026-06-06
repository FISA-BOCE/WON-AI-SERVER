package com.woorifisa.won_ai_server.domain.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woorifisa.won_ai_server.domain.chat.dto.request.ClassifyRequest;
import com.woorifisa.won_ai_server.domain.chat.dto.response.ClassifyResponse;
import com.woorifisa.won_ai_server.domain.chat.external.OpenAiClient;
import com.woorifisa.won_ai_server.global.exception.AiClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ClassifyServiceImpl implements ClassifyService {

    private static final String SYSTEM_PROMPT = """
            너는 금융 챗봇의 의도 분류기야.
            사용자의 질문을 분석해서 아래 형식의 JSON만 반환해.
            설명, 주석, 마크다운 없이 JSON만 반환해.
            [반환 형식]
            {
              "queryType": "...",
              "dbTarget": "MYSQL" or "NEO4J",
              "dataSource": "CARD" or "SECURITIES",
              "confidence": 0.0 ~ 1.0,
              "params": {
                "baseMonth": "YYYY-MM"
              }
            }
            [queryType 목록]
            CARD_MONTHLY_TOTAL_SPEND  - 이번달 카드 결제 총액
            POINT_CURRENT_BALANCE     - 포인트 잔액
            POINT_MONTHLY_EARNED      - 이번달 포인트 적립액
            ETF_LIST                  - 보유 ETF 목록
            ETF_AMOUNT                - ETF 총 매수금액
            SAME_ETF_AVERAGE_POINT    - 나와 같은 ETF를 선택한 사용자들의 평균 포인트 투자금액 (그래프 조회)
            MY_POINT_INVESTMENT_PATH  - 내 포인트가 어떤 ETF 투자 요청으로 이어졌는지 (그래프 조회)
            UNKNOWN                   - 위 항목에 해당하지 않는 질문
            [dbTarget 판단 기준]
            MYSQL: 테이블에 이미 집계된 숫자를 꺼내오면 되는 질문
              예) 총액, 잔액, 적립액, ETF 금액
            NEO4J: A → B → C 관계를 따라가야 답을 구할 수 있는 질문
              예) 포인트 → ETF 전환 흐름
                  같은 ETF 선택한 유저들의 평균 포인트
            [dataSource 판단 기준]
            CARD: 카드 결제, 포인트, 실적, 리워드 관련
              예) 결제 총액, 포인트 잔액, 포인트 적립
            SECURITIES: ETF 보유, 매수금액 관련
              예) ETF 목록, ETF 총액
            단, 포인트 → ETF 전환 흐름은 CARD
            [confidence 기준]
            0.9 이상: 질문이 명확하고 queryType 확실히 매핑됨
            0.7~0.9: 다소 모호하지만 판단 가능
            0.7 미만: 판단 불가 → UNKNOWN 반환
            [params 규칙]
            baseMonth: 항상 포함. 현재 날짜 기준 YYYY-MM 형식
            """;

    private static final Pattern BASE_MONTH_PATTERN = Pattern.compile("^\\d{4}-(0[1-9]|1[0-2])$");

    private final OpenAiClient openAiClient;
    private final ObjectMapper objectMapper;

    @Override
    public ClassifyResponse classify(ClassifyRequest request) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String userMessage = String.format("[오늘 날짜: %s]\n%s", today, request.message());
        String json = openAiClient.callWithJsonResponse(SYSTEM_PROMPT, userMessage);
        try {
            ClassifyResponse response = objectMapper.readValue(json, ClassifyResponse.class);
            validateClassifyResponse(response);
            return response;
        } catch (JsonProcessingException e) {
            throw new AiClientException("classify 응답 파싱 실패: " + e.getMessage(), e);
        }
    }

    private void validateClassifyResponse(ClassifyResponse response) {
        if (response.confidence() < 0.0 || response.confidence() > 1.0) {
            throw new AiClientException("classify 응답의 confidence 값이 유효하지 않습니다: " + response.confidence());
        }
        if (response.params() == null || !response.params().containsKey("baseMonth")) {
            throw new AiClientException("classify 응답에 baseMonth가 누락되었습니다.");
        }
        String baseMonth = response.params().get("baseMonth");
        if (!BASE_MONTH_PATTERN.matcher(baseMonth).matches()) {
            throw new AiClientException("classify 응답의 baseMonth 형식이 올바르지 않습니다: " + baseMonth);
        }
    }
}