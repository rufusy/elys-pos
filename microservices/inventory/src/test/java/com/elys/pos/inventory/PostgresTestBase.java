package com.elys.pos.inventory;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class PostgresTestBase {

    private final static JdbcDatabaseContainer<?> database = new PostgreSQLContainer<>("postgres:17")
            .withConnectTimeoutSeconds(300);

    static {
        database.start();
    }

    @DynamicPropertySource
    static void setDatabaseProps(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", database::getJdbcUrl);
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.datasource.password", database::getPassword);
    }
}
