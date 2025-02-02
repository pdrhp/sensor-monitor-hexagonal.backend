package com.pedroh.sensorapihexagonalstudy.auth.domain.contract;

public record LoginCommand(
    String username,
    String password
) { }
