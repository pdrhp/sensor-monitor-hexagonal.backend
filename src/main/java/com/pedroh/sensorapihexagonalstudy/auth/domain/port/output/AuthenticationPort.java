package com.pedroh.sensorapihexagonalstudy.auth.domain.port.output;

import java.util.concurrent.CompletableFuture;

import com.pedroh.sensorapihexagonalstudy.auth.domain.contract.AuthenticationResult;
import com.pedroh.sensorapihexagonalstudy.auth.domain.contract.LoginCommand;
import com.pedroh.sensorapihexagonalstudy.auth.domain.contract.RegisterUserCommand;


public interface AuthenticationPort {
    CompletableFuture<AuthenticationResult> authenticate(LoginCommand command);
    CompletableFuture<AuthenticationResult> refreshToken(String refreshToken);
    CompletableFuture<Void> registerUser(RegisterUserCommand command);
}

