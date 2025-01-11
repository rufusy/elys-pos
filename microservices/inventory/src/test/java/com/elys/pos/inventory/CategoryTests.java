package com.elys.pos.inventory;

import com.elys.pos.inventory.v1.config.TestSecurityConfig;
import com.elys.pos.inventory.v1.entity.CategoryEntity;
import com.elys.pos.inventory.v1.persistence.PostgresTestBase;
import com.elys.pos.inventory.v1.repository.CategoryRepository;
import com.elys.pos.inventory.v1.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = {TestSecurityConfig.class})
class CategoryTests extends PostgresTestBase {

    @Autowired
    private WebTestClient client;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ItemRepository itemRepository;
    String userId;

    @BeforeEach
    void setupDb() {
        itemRepository.deleteAll();
        categoryRepository.deleteAll();

        userId = UUID.randomUUID().toString();

        String category1Json = """
                    {
                        "name": "cat1",
                        "description": "d1",
                        "createdBy": "%s"
                    }
                """.formatted(userId);
        postAndVerifyCategory(category1Json, CREATED);

        String category2Json = """
                    {
                        "name": "cat2",
                        "description": "d2",
                        "parentCategory": {
                            "name": "cat1"
                        },
                        "createdBy": "%s"
                    }
                """.formatted(userId);
        postAndVerifyCategory(category2Json, CREATED);

    }

    @Test
    void givenValidUrl_whenFetchingCategories_thenPaginatedCategoriesAreReturned() {
        assertEquals(2, categoryRepository.count());
        getAndVerifyCategory(OK)
                .jsonPath("$.totalElements").isEqualTo(2)
                .jsonPath("$.content[0].name").isEqualTo("cat1")
                .jsonPath("$.content[0].createdBy").isEqualTo(userId)
                .jsonPath("$.content[0].updatedBy").isEqualTo(null)
                .jsonPath("$.content[0].deleted").isEqualTo(false)
                .jsonPath("$.content[0].deletedBy").isEqualTo(null)
                .jsonPath("$.content[1].name").isEqualTo("cat2")
                .jsonPath("$.content[1].parentCategory.name").isEqualTo("cat1");
    }

    @Test
    void givenMatchingId_whenFindCategoryById_thenCategoryIsReturned() {
        CategoryEntity entity = categoryRepository.findByName("cat1").orElse(null);
        assertNotNull(entity);
        String categoryId = entity.getId().toString();
        getAndVerifyCategory(categoryId, OK)
                .jsonPath("$.id").isEqualTo(categoryId)
                .jsonPath("$.name").isEqualTo("cat1");
    }

    @Test
    void givenUnknownId_whenFindCategoryById_thenNotFoundErrorIsReturned() {
        String id = UUID.randomUUID().toString();
        getAndVerifyCategory(id, NOT_FOUND)
                .jsonPath("$.message").isEqualTo("No category found for categoryId: " + id);
    }

    @Test
    void givenMissingCreatedBy_whenCreatingCategory_thenUnprocessableEntityErrorIsReturned() {
        String category1Json = """
                    {
                        "name": "cat1",
                        "description": "d1"
                    }
                """;
        postAndVerifyCategory(category1Json, UNPROCESSABLE_ENTITY)
                .jsonPath("$.message").isEqualTo("Missing created by field");
    }

    @Test
    void givenUnknownParentCategory_whenCreatingCategory_thenNotFoundErrorIsReturned() {
        String category1Json = """
                    {
                        "name": "cat1",
                        "description": "d1",
                        "parentCategory": {
                            "name": "unknown"
                        },
                        "createdBy": "%s"
                    }
                """.formatted(userId);
        postAndVerifyCategory(category1Json, NOT_FOUND)
                .jsonPath("$.message").isEqualTo("No category found of name: unknown");
    }

    @Test
    void givenValidPayload_whenUpdatingCategory_thenCategoryIsUpdated() {
        CategoryEntity entity = categoryRepository.findByName("cat2").orElse(null);
        assertNotNull(entity);
        String categoryId = entity.getId().toString();
        String jsonBody = """
                    {
                        "id": "%s",
                        "name": "cat2-updated",
                        "description": "d2-updated",
                        "updatedBy": "%s"
                    }
                """.formatted(categoryId, userId);

        updateAndVerifyCategory(jsonBody, OK)
                .jsonPath("$.id").isEqualTo(categoryId)
                .jsonPath("$.name").isEqualTo("cat2-updated")
                .jsonPath("$.description").isEqualTo("d2-updated")
                .jsonPath("$.parentCategory").isEqualTo(null)
                .jsonPath("$.updatedBy").isEqualTo(userId);
    }

    @Test
    void givenMissingUpdatedBy_whenUpdatingCategory_thenUnprocessableEntityErrorIsReturned() {
        CategoryEntity entity = categoryRepository.findByName("cat2").orElse(null);
        assertNotNull(entity);
        String categoryId = entity.getId().toString();
        String jsonBody = """
                    {
                        "id": "%s",
                        "name": "cat2-updated",
                        "description": "d2-updated"
                    }
                """.formatted(categoryId);
        updateAndVerifyCategory(jsonBody, UNPROCESSABLE_ENTITY)
                .jsonPath("$.message").isEqualTo("Missing updated by field");
    }

    @Test
    void deleteCategory() {
    }

    private WebTestClient.BodyContentSpec getAndVerifyCategory(HttpStatus expectedStatus) {
        return getAndVerifyCategory(null, expectedStatus);
    }

    private WebTestClient.BodyContentSpec getAndVerifyCategory(String id, HttpStatus expectedStatus) {
        String uri = (id == null) ? "/v1/categories" : "/v1/categories/" + id;
        return client.get()
                .uri(uri)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody();
    }

    private WebTestClient.BodyContentSpec postAndVerifyCategory(String jsonBody, HttpStatus expectedStatus) {
        return client.post()
                .uri("/v1/categories")
                .contentType(APPLICATION_JSON) // Explicitly set the Content-Type
                .bodyValue(jsonBody) // Send the raw JSON body
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody();
    }

    private WebTestClient.BodyContentSpec updateAndVerifyCategory(String jsonBody, HttpStatus expectedStatus) {
        return client.put().uri("/v1/categories")
                .contentType(APPLICATION_JSON)
                .bodyValue(jsonBody)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody();
    }

    private void deleteAndVerifyCategory(String id, HttpStatus expectedStatus) {
    }
}
