package com.pedroh.sensorapihexagonalstudy.auth.infrastructure.config;

import com.pedroh.sensorapihexagonalstudy.auth.infrastructure.adapter.keycloak.error.KeycloakResponseErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;

@Configuration
public class KeycloakRestTemplateConfig {

    @Bean("keycloakRestTemplate")
    public RestTemplate keycloakRestTemplate() {
        return new RestTemplateBuilder()
                .errorHandler(new KeycloakResponseErrorHandler())
                .build();
    }
}