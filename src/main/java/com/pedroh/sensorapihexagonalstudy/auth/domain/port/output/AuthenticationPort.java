package com.pedroh.sensorapihexagonalstudy.auth.domain.port.output;

import com.pedroh.sensorapihexagonalstudy.auth.domain.contract.AuthenticationResult;
import com.pedroh.sensorapihexagonalstudy.auth.domain.contract.LoginCommand;

public interface AuthenticationPort {
    AuthenticationResult authenticate(LoginCommand command);
    AuthenticationResult refreshToken(String refreshToken);
}
