package com.elys.pos.inventory.persistence;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class PostgresTestBase {

    private static final String POSTGRES_VERSION = "postgres:17";

    private static final JdbcDatabaseContainer<?> database = new PostgreSQLContainer<>(POSTGRES_VERSION)
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
