package com.pedroh.sensorapihexagonalstudy.auth.infrastructure.adapter.keycloak.error;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import java.io.IOException;
import java.net.URI;

public class KeycloakResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is4xxClientError() ||
                response.getStatusCode().is5xxServerError();
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().is4xxClientError()) {
            throw new KeycloakAuthenticationException("Falha na autenticação com Keycloak: " +
                    method + " " + url);
        }
        throw new KeycloakServerException("Erro no servidor do Keycloak: " +
                method + " " + url);
    }
}