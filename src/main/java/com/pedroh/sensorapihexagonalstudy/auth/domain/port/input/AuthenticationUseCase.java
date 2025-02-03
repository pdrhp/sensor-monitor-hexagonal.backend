package com.pedroh.sensorapihexagonalstudy.auth.domain.port.input;

import java.util.concurrent.CompletableFuture;

import com.pedroh.sensorapihexagonalstudy.auth.domain.contract.AuthenticationResult;
import com.pedroh.sensorapihexagonalstudy.auth.domain.contract.LoginCommand;
import com.pedroh.sensorapihexagonalstudy.auth.domain.contract.RegisterUserCommand;


public interface AuthenticationUseCase {
    CompletableFuture<AuthenticationResult> authenticate(LoginCommand command);
    CompletableFuture<AuthenticationResult> refreshToken(String refreshToken);
    CompletableFuture<Void> registerUser(RegisterUserCommand registerUser);
}
