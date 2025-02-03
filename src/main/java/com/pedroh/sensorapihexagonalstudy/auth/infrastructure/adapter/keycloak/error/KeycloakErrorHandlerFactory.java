package com.pedroh.sensorapihexagonalstudy.auth.infrastructure.adapter.keycloak.error;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class KeycloakErrorHandlerFactory {
    
    public ExchangeFilterFunction createErrorHandler() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (clientResponse.statusCode().is4xxClientError()) {
                return handleClientError(clientResponse);
            }
            if (clientResponse.statusCode().is5xxServerError()) {
                return handleServerError(clientResponse);
            }
            return Mono.just(clientResponse);
        });
    }

    private Mono<ClientResponse> handleClientError(ClientResponse response) {
        return response.bodyToMono(String.class)
                .flatMap(error -> {
                    log.error("Erro de cliente (4xx) na requisição ao Keycloak: {}", error);
                    return Mono.error(new KeycloakAuthenticationException(
                            "Falha na autenticação com Keycloak: " + 
                            response.statusCode().value() + " - " + error
                    ));
                });
    }

    private Mono<ClientResponse> handleServerError(ClientResponse response) {
        return response.bodyToMono(String.class)
                .flatMap(error -> {
                    log.error("Erro de servidor (5xx) na requisição ao Keycloak: {}", error);
                    return Mono.error(new KeycloakServerException(
                            "Erro no servidor do Keycloak: " + 
                            response.statusCode().value() + " - " + error
                    ));
                });
    }
}