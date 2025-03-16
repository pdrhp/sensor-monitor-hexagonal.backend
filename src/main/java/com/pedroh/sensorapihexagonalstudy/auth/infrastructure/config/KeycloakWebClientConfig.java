package com.pedroh.sensorapihexagonalstudy.auth.infrastructure.config;

import com.pedroh.sensorapihexagonalstudy.auth.infrastructure.adapter.keycloak.error.KeycloakErrorHandlerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class KeycloakWebClientConfig {

    private final KeycloakErrorHandlerFactory errorHandlerFactory;
    private final String keycloakIssuerUri;

    public KeycloakWebClientConfig(KeycloakErrorHandlerFactory errorHandlerFactory,
                                   @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String keycloakIssuerUri) {
        this.errorHandlerFactory = errorHandlerFactory;
        this.keycloakIssuerUri = keycloakIssuerUri;
    }

    @Bean("keycloakWebClient")
    public WebClient keycloakWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(keycloakIssuerUri)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .filter(errorHandlerFactory.createErrorHandler())
                .build();
    }
}
