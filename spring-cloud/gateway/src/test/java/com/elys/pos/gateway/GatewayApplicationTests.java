package com.elys.pos.gateway;

import com.elys.pos.gateway.v1.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = {TestSecurityConfig.class})
class GatewayApplicationTests {

	@Test
	void contextLoads() {
	}

}
