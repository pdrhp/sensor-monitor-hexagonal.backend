package com.pedroh.sensorapihexagonalstudy.auth.infrastructure.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pedroh.sensorapihexagonalstudy.auth.domain.contract.AuthenticationResult;
import com.pedroh.sensorapihexagonalstudy.auth.domain.contract.LoginCommand;
import com.pedroh.sensorapihexagonalstudy.auth.domain.port.input.AuthenticationUseCase;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationUseCase authenticationUseCase;

    public AuthController(AuthenticationUseCase authenticationUseCase) {
        this.authenticationUseCase = authenticationUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResult> login(@RequestBody LoginCommand loginCommand) {
        logger.info("Tentativa de login para usuário: {}", loginCommand.username());
        return ResponseEntity.ok(authenticationUseCase.authenticate(loginCommand));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResult> refreshToken(
            @RequestHeader("X-Refresh-Token") String refreshToken
    ) {
        logger.info("Tentativa de refresh token");
        return ResponseEntity.ok(authenticationUseCase.refreshToken(refreshToken));
    }
}