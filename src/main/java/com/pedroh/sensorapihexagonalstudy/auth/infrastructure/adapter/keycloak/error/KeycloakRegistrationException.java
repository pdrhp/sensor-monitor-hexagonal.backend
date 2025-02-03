package com.pedroh.sensorapihexagonalstudy.auth.infrastructure.adapter.keycloak.error;

import com.pedroh.sensorapihexagonalstudy.auth.infrastructure.web.exception.AuthErrorCode;
import com.pedroh.sensorapihexagonalstudy.shared.infrastructure.web.exception.ApiException;
import org.springframework.http.HttpStatus;

public class KeycloakRegistrationException extends ApiException {
    public KeycloakRegistrationException(String message) {
        super(message, AuthErrorCode.REGISTRATION_ERROR, HttpStatus.BAD_REQUEST);
    }
}
