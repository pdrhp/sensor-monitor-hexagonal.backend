package com.pedroh.sensorapihexagonalstudy.auth.infrastructure.adapter.keycloak;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.pedroh.sensorapihexagonalstudy.auth.domain.contract.AuthenticationResult;
import com.pedroh.sensorapihexagonalstudy.auth.domain.contract.LoginCommand;
import com.pedroh.sensorapihexagonalstudy.auth.domain.port.output.AuthenticationPort;
import com.pedroh.sensorapihexagonalstudy.auth.infrastructure.adapter.keycloak.dto.KeycloakTokenResponse;
import com.pedroh.sensorapihexagonalstudy.auth.infrastructure.config.KeycloakProperties;


@Component
public class KeycloakAuthenticationAdapter implements AuthenticationPort {

    private final RestTemplate restTemplate;
    private final String tokenEndpoint;
    private final String clientId;
    private final String clientSecret;

    public KeycloakAuthenticationAdapter(
            @Qualifier("keycloakRestTemplate") RestTemplate restTemplate,
            @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri,
            KeycloakProperties keycloakProperties
    ) {
        this.restTemplate = restTemplate;
        this.tokenEndpoint = issuerUri + "/protocol/openid-connect/token";
        this.clientId = keycloakProperties.getClientId();
        this.clientSecret = keycloakProperties.getClientSecret();
    }


    @Override
    public AuthenticationResult authenticate(LoginCommand command) {
        var requestBody = new LinkedMultiValueMap<String, String>();
        requestBody.add("grant_type", "password");
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("username", command.username());
        requestBody.add("password", command.password());

        return executeTokenRequest(requestBody);
    }

    @Override
    public AuthenticationResult refreshToken(String refreshToken) {
        var requestBody = new LinkedMultiValueMap<String, String>();
        requestBody.add("grant_type", "refresh_token");
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("refresh_token", refreshToken);

        return executeTokenRequest(requestBody);
    }

    private AuthenticationResult executeTokenRequest(MultiValueMap<String, String> requestBody){
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        var request = new HttpEntity<>(requestBody, headers);

        var response = restTemplate.postForEntity(
                tokenEndpoint,
                request,
                KeycloakTokenResponse.class
        );

        if(response.getBody() == null){
            throw new RuntimeException("Failed to authenticate with Keycloak");
        }

        return new AuthenticationResult(
                response.getBody().accessToken(),
                response.getBody().refreshToken(),
                response.getBody().expiresIn()
        );
    }
}
