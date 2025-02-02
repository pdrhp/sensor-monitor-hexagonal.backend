package com.pedroh.sensorapihexagonalstudy.shared.infrastructure.web.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        String code,
        String error,
        String message,
        String timestamp,
        String path
) {
    public static ErrorResponse of(String code, String error, String message, String path) {
        return new ErrorResponse(
                code,
                error,
                message,
                LocalDateTime.now().toString(),
                path
        );
    }
}