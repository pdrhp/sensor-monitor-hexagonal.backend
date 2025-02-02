package com.pedroh.sensorapihexagonalstudy.auth.infrastructure.web.exception;

import com.pedroh.sensorapihexagonalstudy.shared.infrastructure.web.exception.ErrorCode;

public enum AuthErrorCode implements ErrorCode {
    INVALID_CREDENTIALS("AUTH001", "Credenciais inválidas"),
    TOKEN_EXPIRED("AUTH002", "Token expirado"),
    INVALID_TOKEN("AUTH003", "Token inválido"),
    KEYCLOAK_ERROR("AUTH004", "Erro no servidor de autenticação");

    private final String code;
    private final String defaultMessage;

    AuthErrorCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDefaultMessage() {
        return defaultMessage;
    }
}