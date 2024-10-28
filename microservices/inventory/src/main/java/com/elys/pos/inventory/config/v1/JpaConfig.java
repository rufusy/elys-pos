package com.elys.pos.inventory.config.v1;

import com.elys.pos.inventory.repository.v1.SoftDeleteRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@Configuration
//@EnableJpaRepositories(
//        basePackages = "com.elys.pos.inventory.repository.v1",
//        repositoryBaseClass = SoftDeleteRepositoryImpl.class
//)
public class JpaConfig {
    // Custom JPA repository configuration
}
