package com.elys.pos.gateway.v1.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final ReactiveJwtDecoder jwtDecoder;

    public SecurityConfig(@Qualifier("customJwtDecoder") ReactiveJwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf(csrf -> csrf.disable())
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/headerrouting/**").permitAll()
                        .pathMatchers("/actuator/**").permitAll()
                        .pathMatchers("/eureka/**").permitAll()
                        .pathMatchers("/oauth2/**").permitAll()
                        .pathMatchers("/realms/**").permitAll()
                        .pathMatchers("/resources/**").permitAll()
                        .pathMatchers("/robots/**").permitAll()
                        .pathMatchers("/config/**").permitAll()
                        .pathMatchers("/v1/openapi/**").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(server ->
                        server.jwt(jwt -> jwt.jwtDecoder(jwtDecoder)));  // Use custom JwtDecoder

        return http.build();
    }
}
