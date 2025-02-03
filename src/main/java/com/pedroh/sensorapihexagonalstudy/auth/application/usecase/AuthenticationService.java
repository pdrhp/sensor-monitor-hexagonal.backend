package com.pedroh.sensorapihexagonalstudy.auth.application.usecase;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.pedroh.sensorapihexagonalstudy.auth.domain.contract.AuthenticationResult;
import com.pedroh.sensorapihexagonalstudy.auth.domain.contract.LoginCommand;
import com.pedroh.sensorapihexagonalstudy.auth.domain.contract.RegisterUserCommand;
import com.pedroh.sensorapihexagonalstudy.auth.domain.port.input.AuthenticationUseCase;
import com.pedroh.sensorapihexagonalstudy.auth.domain.port.output.AuthenticationPort;

@Service
public class AuthenticationService implements AuthenticationUseCase {

    private final AuthenticationPort authenticationPort;

    public AuthenticationService(AuthenticationPort authenticationPort) {
        this.authenticationPort = authenticationPort;
    }

    @Override
    public CompletableFuture<AuthenticationResult> authenticate(LoginCommand command) {
        return authenticationPort.authenticate(command);
    }

    @Override
    public CompletableFuture<AuthenticationResult> refreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalArgumentException("Refresh token não pode ser nulo ou vazio");
        }
        return authenticationPort.refreshToken(refreshToken);
    }

    @Override
    public CompletableFuture<Void> registerUser(RegisterUserCommand command) {
        return authenticationPort.registerUser(command);
    }
}
