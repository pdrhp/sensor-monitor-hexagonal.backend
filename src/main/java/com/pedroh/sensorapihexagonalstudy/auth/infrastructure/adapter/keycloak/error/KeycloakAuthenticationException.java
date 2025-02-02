package com.pedroh.sensorapihexagonalstudy.auth.infrastructure.adapter.keycloak.error;

import com.pedroh.sensorapihexagonalstudy.auth.infrastructure.web.exception.AuthErrorCode;
import com.pedroh.sensorapihexagonalstudy.shared.infrastructure.web.exception.ApiException;
import org.springframework.http.HttpStatus;

public class KeycloakAuthenticationException extends ApiException {

    public KeycloakAuthenticationException(String message) {
        super(message, AuthErrorCode.INVALID_CREDENTIALS, HttpStatus.UNAUTHORIZED);
    }
}