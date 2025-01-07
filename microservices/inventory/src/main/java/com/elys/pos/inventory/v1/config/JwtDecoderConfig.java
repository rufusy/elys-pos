package com.elys.pos.inventory.v1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;

import java.util.List;

@Configuration
public class JwtDecoderConfig {

    @Bean(name = "customJwtDecoder")
    public ReactiveJwtDecoder jwtDecoder() {
        String jwkSetUri = "http://keycloak:8080/realms/demo/protocol/openid-connect/certs";

        NimbusReactiveJwtDecoder jwtDecoder = NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();

        // Create a custom OAuth2TokenValidator for issuer validation
        OAuth2TokenValidator<Jwt> issuerValidator = token -> {
            String expectedIssuer = "http://localhost:8086/realms/demo";
            if (!token.getIssuer().toString().equals(expectedIssuer)) {
                return OAuth2TokenValidatorResult.failure(new OAuth2Error(
                        "invalid_issuer",
                        "The 'iss' claim is invalid. Expected: " + expectedIssuer,
                        null
                ));
            }
            return OAuth2TokenValidatorResult.success();
        };

        OAuth2TokenValidator<Jwt> validator = JwtValidators.createDefaultWithValidators(List.of(issuerValidator));

        jwtDecoder.setJwtValidator(validator);

        return jwtDecoder;
    }
}
