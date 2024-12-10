package com.elys.pos.inventory.v1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/headerrouting/**").permitAll()
                        .pathMatchers("/openapi/**").permitAll()
                        .pathMatchers("/webjars/**").permitAll()
                        .pathMatchers("/actuator/**").permitAll()
                        .pathMatchers(GET, "/v1/categories/**").hasAuthority("SCOPE_category:read")
                        .pathMatchers(POST, "/v1/categories/**").hasAuthority("SCOPE_category:create")
                        .pathMatchers(PUT, "/v1/categories/**").hasAuthority("SCOPE_category:update")
                        .pathMatchers(DELETE, "/v1/categories/**").hasAuthority("SCOPE_category:delete")
                        .pathMatchers(GET, "/v1/items/**").hasAuthority("SCOPE_item:read")
                        .pathMatchers(POST, "/v1/items/**").hasAuthority("SCOPE_item:create")
                        .pathMatchers(PUT, "/v1/items/**").hasAuthority("SCOPE_item:update")
                        .pathMatchers(DELETE, "/v1/items/**").hasAuthority("SCOPE_item:delete")
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(resourceServer -> resourceServer.jwt(Customizer.withDefaults()));

        return http.build();
    }
}
