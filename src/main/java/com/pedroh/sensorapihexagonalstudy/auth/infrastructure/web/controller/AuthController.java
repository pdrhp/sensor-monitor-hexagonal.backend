package com.pedroh.sensorapihexagonalstudy.auth.infrastructure.web.controller;

import com.pedroh.sensorapihexagonalstudy.auth.domain.contract.RegisterUserCommand;
import com.pedroh.sensorapihexagonalstudy.auth.infrastructure.adapter.keycloak.error.KeycloakAuthenticationException;
import com.pedroh.sensorapihexagonalstudy.auth.infrastructure.adapter.keycloak.error.KeycloakRegistrationException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pedroh.sensorapihexagonalstudy.auth.domain.contract.AuthenticationResult;
import com.pedroh.sensorapihexagonalstudy.auth.domain.contract.LoginCommand;
import com.pedroh.sensorapihexagonalstudy.auth.domain.port.input.AuthenticationUseCase;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final AuthenticationUseCase authenticationUseCase;

    public AuthController(AuthenticationUseCase authenticationUseCase) {
        this.authenticationUseCase = authenticationUseCase;
    }

    @PostMapping("/login")
    public CompletableFuture<ResponseEntity<AuthenticationResult>> login(
            @RequestBody @Valid LoginCommand loginCommand
    ) {
        log.info("Tentativa de login para usuário: {}", loginCommand.username());
        return authenticationUseCase
                .authenticate(loginCommand)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/refresh")
    public CompletableFuture<ResponseEntity<AuthenticationResult>> refreshToken(
            @RequestHeader("X-Refresh-Token") String refreshToken
    ) {
        log.info("Tentativa de refresh token");
        return authenticationUseCase
                .refreshToken(refreshToken)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/register")
    public CompletableFuture<ResponseEntity<Void>> register(
            @RequestBody @Valid RegisterUserCommand command
    ) {
        log.info("Tentativa de registro para usuário: {}", command.username());
        return authenticationUseCase
                .registerUser(command)
                .thenApply(result -> ResponseEntity.status(HttpStatus.CREATED).build());
    }

}