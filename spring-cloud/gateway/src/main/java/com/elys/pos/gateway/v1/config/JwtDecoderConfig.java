package com.elys.pos.gateway.v1.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import java.util.List;

@Configuration
public class JwtDecoderConfig {

    private final String expectedIssuer;
    private final String jwkSetUri;

    public JwtDecoderConfig(@Value("${app.auth-server-iss}") String expectedIssuer,
                            @Value("${app.auth-server-jwks-uri}") String jwkSetUri) {
        this.expectedIssuer = expectedIssuer;
        this.jwkSetUri = jwkSetUri;
    }

    @Bean(name = "customJwtDecoder")
    public ReactiveJwtDecoder jwtDecoder() {

        NimbusReactiveJwtDecoder jwtDecoder = NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();

        // Create a custom OAuth2TokenValidator for issuer validation
        OAuth2TokenValidator<Jwt> issuerValidator = token -> {
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


