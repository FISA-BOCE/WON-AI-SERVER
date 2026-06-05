package com.woorifisa.won_ai_server.global.exception;

import com.woorifisa.won_ai_server.global.exception.AiClientException;
import com.woorifisa.won_ai_server.global.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AiClientException.class)
    public ResponseEntity<ApiResponse<Void>> handleAiClientException(AiClientException e) {
        log.error("Azure OpenAI 호출 오류: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError()
                .body(new ApiResponse<>(500, e.getMessage(), null));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException e) {
        log.error("내부 서버 오류: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError()
                .body(new ApiResponse<>(500, e.getMessage(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest()
                .body(new ApiResponse<>(400, message, null));
    }
}
