package com.elys.pos.inventory.v1.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class DataJpaTestConfig {
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return Mockito.mock(ObjectMapper.class);
    }
}
