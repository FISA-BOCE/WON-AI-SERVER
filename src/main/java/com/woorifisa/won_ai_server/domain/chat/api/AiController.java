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
    public ApiResponse<ClassifyResponse> classify(
            @RequestHeader(value = "X-Transaction-ID", required = false) String transactionId,
            @Valid @RequestBody ClassifyRequest request
    ) {
        return ApiResponse.ok(classifyService.classify(request));
    }

    @PostMapping("/answer")
    public ApiResponse<AnswerResponse> answer(
            @RequestHeader(value = "X-Transaction-ID", required = false) String transactionId,
            @Valid @RequestBody AnswerRequest request
    ) {
        return ApiResponse.ok(answerService.generateAnswer(request));
    }
}
