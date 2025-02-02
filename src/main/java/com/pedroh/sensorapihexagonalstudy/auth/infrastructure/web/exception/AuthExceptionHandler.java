package com.pedroh.sensorapihexagonalstudy.auth.infrastructure.web.exception;

import com.pedroh.sensorapihexagonalstudy.auth.infrastructure.adapter.keycloak.error.KeycloakAuthenticationException;
import com.pedroh.sensorapihexagonalstudy.shared.infrastructure.web.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice("com.pedroh.sensorapihexagonalstudy.auth")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(AuthExceptionHandler.class);

    @ExceptionHandler(KeycloakAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleKeycloakAuthenticationException(
            KeycloakAuthenticationException ex,
            WebRequest request
    ) {
        logger.error("Keycloak authentication error: {}", ex.getMessage());

        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(ErrorResponse.of(
                        AuthErrorCode.INVALID_CREDENTIALS.getCode(),
                        AuthErrorCode.INVALID_CREDENTIALS.getDefaultMessage(),
                        ex.getMessage(),
                        request.getDescription(false)
                ));
    }
}