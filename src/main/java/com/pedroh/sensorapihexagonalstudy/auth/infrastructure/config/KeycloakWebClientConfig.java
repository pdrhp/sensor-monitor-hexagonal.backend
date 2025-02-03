package com.pedroh.sensorapihexagonalstudy.auth.infrastructure.config;

import com.pedroh.sensorapihexagonalstudy.auth.infrastructure.adapter.keycloak.error.KeycloakErrorHandlerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class KeycloakWebClientConfig {

    private final KeycloakErrorHandlerFactory errorHandlerFactory;

    public KeycloakWebClientConfig(KeycloakErrorHandlerFactory errorHandlerFactory) {
        this.errorHandlerFactory = errorHandlerFactory;
    }


    @Bean("keycloakWebClient")
    public WebClient keycloakWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("seu-url-base-do-keycloak")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .filter(errorHandlerFactory.createErrorHandler())
                .build();
    }
}
