package com.pedroh.sensorapihexagonalstudy.auth.domain.contract;

public record AuthenticationResult(
        String accessToken,
        String refreshToken,
        Long expiresIn
) { }
