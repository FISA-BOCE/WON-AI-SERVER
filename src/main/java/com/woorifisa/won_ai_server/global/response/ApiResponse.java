package com.woorifisa.won_ai_server.global.response;

public record ApiResponse<T>(int status, String message, T data) {
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(200, "OK", data);
    }
}