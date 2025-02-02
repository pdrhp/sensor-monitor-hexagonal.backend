package com.pedroh.sensorapihexagonalstudy.auth.domain.port.input;

import com.pedroh.sensorapihexagonalstudy.auth.domain.contract.AuthenticationResult;
import com.pedroh.sensorapihexagonalstudy.auth.domain.contract.LoginCommand;

public interface AuthenticationUseCase {
    AuthenticationResult authenticate(LoginCommand command);
    AuthenticationResult refreshToken(String refreshToken);
}
