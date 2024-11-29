package com.elys.pos.eurekaserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EurekaServerApplicationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Value("${app.eureka-username}")
    private String username;

    @Value("${app.eureka-password}")
    private String password;

    @Test
    void healthy() {
        TestRestTemplate restTemplateWithAuth = testRestTemplate.withBasicAuth(username, password);

        String expectedResponseBody = "{\"status\":\"UP\"}";
        ResponseEntity<String> responseEntity = restTemplateWithAuth.getForEntity("/actuator/health", String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponseBody, responseEntity.getBody());
    }

    @Test
    void catalogLoads() {
        TestRestTemplate restTemplateWithAuth = testRestTemplate.withBasicAuth(username, password);

        String expectedResponseBody = "{\"applications\":{\"versions__delta\":\"1\",\"apps__hashcode\":\"\",\"application\":[]}}";
        ResponseEntity<String> responseEntity = restTemplateWithAuth.getForEntity("/eureka/apps", String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponseBody, responseEntity.getBody());
    }
}
