package com.pedroh.sensorapihexagonalstudy.auth.application.usecase;

import com.pedroh.sensorapihexagonalstudy.auth.domain.contract.AuthenticationResult;
import com.pedroh.sensorapihexagonalstudy.auth.domain.contract.LoginCommand;
import com.pedroh.sensorapihexagonalstudy.auth.domain.port.input.AuthenticationUseCase;
import com.pedroh.sensorapihexagonalstudy.auth.domain.port.output.AuthenticationPort;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements AuthenticationUseCase {

    private final AuthenticationPort authenticationPort;

    public AuthenticationService(AuthenticationPort authenticationPort) {
        this.authenticationPort = authenticationPort;
    }

    @Override
    public AuthenticationResult authenticate(LoginCommand command) {
        return authenticationPort.authenticate(command);
    }

    @Override
    public AuthenticationResult refreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalArgumentException("Refresh token não pode ser nulo ou vazio");
        }
        return authenticationPort.refreshToken(refreshToken);
    }
}
