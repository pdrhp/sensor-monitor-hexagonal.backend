package com.pedroh.sensorapihexagonalstudy.auth.domain.contract;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserCommand(
        @NotBlank(message = "Username é obrigatório")
        String username,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
        String password,

        @NotBlank(message = "Nome é obrigatório")
        String firstName,

        @NotBlank(message = "Sobrenome é obrigatório")
        String lastName
) {}