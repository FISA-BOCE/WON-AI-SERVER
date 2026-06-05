package com.woorifisa.won_ai_server.domain.chat.api;

import com.woorifisa.won_ai_server.domain.chat.dto.request.AnswerRequest;
import com.woorifisa.won_ai_server.domain.chat.dto.request.ClassifyRequest;
import com.woorifisa.won_ai_server.domain.chat.dto.response.AnswerResponse;
import com.woorifisa.won_ai_server.domain.chat.dto.response.ClassifyResponse;
import com.woorifisa.won_ai_server.domain.chat.service.AnswerService;
import com.woorifisa.won_ai_server.domain.chat.service.ClassifyService;
import com.woorifisa.won_ai_server.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AiController {

    private final ClassifyService classifyService;
    private final AnswerService answerService;

    @PostMapping("/classify")
    public ClassifyResponse classify(
            @RequestHeader("X-Transaction-ID") String transactionId,
            @Valid @RequestBody ClassifyRequest request
    ) {
        return classifyService.classify(request);
    }

    @PostMapping("/answer")
    public ApiResponse<AnswerResponse> answer(
            @RequestHeader("X-Transaction-ID") String transactionId,
            @RequestBody AnswerRequest request
    ) {
        return ApiResponse.ok(answerService.generateAnswer(request));
    }
}
