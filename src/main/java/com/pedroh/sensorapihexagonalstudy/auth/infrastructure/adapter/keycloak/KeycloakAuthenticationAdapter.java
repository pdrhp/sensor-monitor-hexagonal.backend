package com.pedroh.sensorapihexagonalstudy.auth.infrastructure.adapter.keycloak;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.pedroh.sensorapihexagonalstudy.auth.domain.contract.AuthenticationResult;
import com.pedroh.sensorapihexagonalstudy.auth.domain.contract.LoginCommand;
import com.pedroh.sensorapihexagonalstudy.auth.domain.contract.RegisterUserCommand;
import com.pedroh.sensorapihexagonalstudy.auth.domain.port.output.AuthenticationPort;
import com.pedroh.sensorapihexagonalstudy.auth.infrastructure.adapter.keycloak.dto.KeycloakTokenResponse;
import com.pedroh.sensorapihexagonalstudy.auth.infrastructure.config.KeycloakProperties;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KeycloakAuthenticationAdapter implements AuthenticationPort {

    private final WebClient webClient;
    private final String tokenEndpoint;
    private final String adminEndpoint;
    private final String clientId;
    private final String clientSecret;

    public KeycloakAuthenticationAdapter(
            @Qualifier("keycloakWebClient") WebClient webClient,
            @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri,
            KeycloakProperties keycloakProperties
    ) {
        this.webClient = webClient;
        this.tokenEndpoint = issuerUri + "/protocol/openid-connect/token";
        this.adminEndpoint = issuerUri.replace("/realms/", "/admin/realms/");
        this.clientId = keycloakProperties.getClientId();
        this.clientSecret = keycloakProperties.getClientSecret();
    }

    @Override
    public CompletableFuture<AuthenticationResult> authenticate(LoginCommand command) {
        var formData = new LinkedMultiValueMap<String, String>();
        formData.add("grant_type", "password");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("username", command.username());
        formData.add("password", command.password());

        return webClient.post()
                .uri(tokenEndpoint)
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(KeycloakTokenResponse.class)
                .map(response -> new AuthenticationResult(
                        response.accessToken(),
                        response.refreshToken(),
                        response.expiresIn()
                ))
                .doOnSuccess(result -> log.info("Autenticação bem sucedida para usuário: {}", command.username()))
                .toFuture();
    }

    @Override
    public CompletableFuture<AuthenticationResult> refreshToken(String refreshToken) {
        var formData = new LinkedMultiValueMap<String, String>();
        formData.add("grant_type", "refresh_token");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("refresh_token", refreshToken);

        return webClient.post()
                .uri(tokenEndpoint)
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(KeycloakTokenResponse.class)
                .map(response -> new AuthenticationResult(
                        response.accessToken(),
                        response.refreshToken(),
                        response.expiresIn()
                ))
                .doOnSuccess(result -> log.info("Token renovado com sucesso"))
                .toFuture();
    }

    @Override
    public CompletableFuture<Void> registerUser(RegisterUserCommand command) {
        return getServiceAccountToken()
                .thenCompose(token -> createUser(command, token));
    }

    private CompletableFuture<String> getServiceAccountToken() {
        var formData = new LinkedMultiValueMap<String, String>();
        formData.add("grant_type", "client_credentials");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);

        return webClient.post()
                .uri(tokenEndpoint)
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(KeycloakTokenResponse.class)
                .map(KeycloakTokenResponse::accessToken)
                .doOnSuccess(token -> log.debug("Token de serviço obtido com sucesso"))
                .toFuture();
    }

    private CompletableFuture<Void> createUser(RegisterUserCommand command, String token) {
        return webClient.post()
                .uri(adminEndpoint + "/users")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createUserPayload(command))
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(v -> log.info("Usuário {} criado com sucesso", command.username()))
                .doOnError(error -> log.error("Erro ao criar usuário: {}", error.getMessage()))
                .then()
                .toFuture();
    }

    private Map<String, Object> createUserPayload(RegisterUserCommand command) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("username", command.username());
        payload.put("email", command.email());
        payload.put("enabled", true);
        payload.put("firstName", command.firstName());
        payload.put("lastName", command.lastName());
        
        List<Map<String, Object>> credentials = new ArrayList<>();
        Map<String, Object> credential = new HashMap<>();
        credential.put("type", "password");
        credential.put("value", command.password());
        credential.put("temporary", false);
        credentials.add(credential);
        
        payload.put("credentials", credentials);
        
        return payload;
    }
}
