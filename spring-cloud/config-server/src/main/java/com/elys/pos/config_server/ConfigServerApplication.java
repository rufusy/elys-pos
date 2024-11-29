package com.elys.pos.config_server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.ConfigurableApplicationContext;

@EnableConfigServer
@SpringBootApplication
public class ConfigServerApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigServerApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ConfigServerApplication.class, args);

        String repoLocation = context.getEnvironment().getProperty("spring.cloud.config.server.native.searchLocations");
        LOGGER.info("Serving configurations from folder: {}", repoLocation);
    }

}
