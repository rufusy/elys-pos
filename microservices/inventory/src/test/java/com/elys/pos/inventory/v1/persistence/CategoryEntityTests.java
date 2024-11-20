package com.elys.pos.inventory.v1.persistence;

import com.elys.pos.inventory.v1.config.DataJpaTestConfig;
import com.elys.pos.inventory.v1.entity.CategoryEntity;
import com.elys.pos.inventory.v1.filter.CategoryFilterOptions;
import com.elys.pos.inventory.v1.repository.CategoryRepository;
import com.elys.pos.inventory.v1.repository.ItemRepository;
import com.elys.pos.inventory.v1.specification.CategorySpecification;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({DataJpaTestConfig.class, CategorySpecification.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryEntityTests extends PostgresTestBase {

    @Autowired
    private CategoryRepository repository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CategorySpecification categorySpecification;
    private CategoryEntity savedEntity;
    private CategoryEntity category1;
    private CategoryEntity category2;
    private UUID user1, user2;

    @BeforeEach
    void setupDb() {
        user1 = UUID.randomUUID();
        user2 = UUID.randomUUID();

        itemRepository.deleteAll();
        assertEquals(0, itemRepository.count());

        repository.deleteAll();
        assertEquals(0, repository.count());

        CategoryEntity entity = CategoryEntity.builder().name("n1").description("d1").createdBy(user1)
                .createdAt(LocalDateTime.now()).build();
        savedEntity = repository.save(entity);

        assertEquals(0, savedEntity.getVersion());
        assertNull(savedEntity.getUpdatedAt());
        assertNull(savedEntity.getUpdatedBy());
        assertNull(savedEntity.getDeletedBy());
        assertNull(savedEntity.getDeletedAt());
        assertFalse(savedEntity.isDeleted());
        assertEqualsCategory(entity, savedEntity);

        category1 = repository.save(
                CategoryEntity.builder().name("Electronics").description("d1").createdBy(user1)
                        .createdAt(LocalDateTime.now()).build()
        );

        category2 = repository.save(
                CategoryEntity.builder().name("Phones and Tablets").description("d1")
                        .parentCategory(category1).createdBy(user1).createdAt(LocalDateTime.now()).build()
        );
    }

    @Test
    void whenSavingCategoryEntityWithNoValidationErrors_thenSuccess() {
        CategoryEntity entity = CategoryEntity.builder().name("n2").description("d2").parentCategory(savedEntity)
                .createdBy(user1).createdAt(LocalDateTime.now()).build();
        repository.save(entity);

        CategoryEntity foundEntity = repository.findById(entity.getId()).orElse(null);
        assert foundEntity != null;
        assertEquals(savedEntity.getId(), foundEntity.getParentCategory().getId()); // verify parent/child relationship
        assertEqualsCategory(entity, foundEntity);
    }

    @Test
    void whenUpdatingCategoryEntityWithNoValidationErrors_thenSuccess() {
        savedEntity.setName("n1 updated");
        savedEntity.setDescription("d1 updated");
        savedEntity.setUpdatedBy(user2);
        savedEntity.setUpdatedAt(LocalDateTime.now());
        repository.save(savedEntity);

        CategoryEntity foundEntity = repository.findById(savedEntity.getId()).get();

        assertEquals(1, foundEntity.getVersion());
        assertEquals("n1 updated", foundEntity.getName());
        assertEquals("d1 updated", foundEntity.getDescription());
        assertEquals(user2, foundEntity.getUpdatedBy());
        assertNotNull(foundEntity.getUpdatedAt());
    }

    @Test
    void whenSoftDeletingAnItemEntity_thenItIsOnlyMarkedAsDeletedButRetained() {
        repository.flagAsDeleted(savedEntity.getId(), user2, LocalDateTime.now());

        // verify that category was not removed from db
        CategoryEntity foundEntity = repository.findById(savedEntity.getId()).orElse(null);
        assertNotNull(foundEntity);

        // verify soft delete fields
        assertTrue(foundEntity.isDeleted());
        assertEquals(user2, foundEntity.getDeletedBy());
        assertNotNull(foundEntity.getDeletedAt());
    }

    @Test
    void whenAccessingLazyRelationsOutOfSession_thenThrowsException() {
        // Fetch the category without accessing items
        CategoryEntity foundEntity = repository.findById(savedEntity.getId()).orElse(null);
        assertNotNull(foundEntity);

        // Try accessing items or kits after the session has been closed,
        // This should throw an exception
        assertThrows(LazyInitializationException.class, () -> assertThat(foundEntity.getItems(), hasSize(0)));

        assertThrows(LazyInitializationException.class, () -> assertThat(foundEntity.getKits(), hasSize(0)));
    }

    @Test
    void whenAccessingEagerRelations_thenSuccess() {
        CategoryEntity foundEntity = repository.findById(savedEntity.getId()).orElse(null);
        assertNotNull(foundEntity);
        assertThat(foundEntity.getAttributeLinks(), hasSize(0));
    }

    @Test
    void whenUpdatingCategoryEntityWithLowerVersion_thenThrowsException() {
        // Store the saved entity in two separate entity objects
        CategoryEntity entity1 = repository.findById(savedEntity.getId()).get();
        CategoryEntity entity2 = repository.findById(savedEntity.getId()).get();

        // Update the entity using the first entity object
        entity1.setName("name updated");
        repository.save(entity1);

        // Update the entity using the second entity object
        // This should fail since the second entity now holds an old version number, i.e. an Optimistic Lock Error
        assertThrows(OptimisticLockingFailureException.class, () -> {
            entity2.setName("name updated");
            repository.save(entity2);
        });

        CategoryEntity updatedEntity = repository.findById(savedEntity.getId()).get();
        assertEquals(1, updatedEntity.getVersion());
        assertEquals("name updated", updatedEntity.getName());
    }

    @Test
    void whenSavingCategoryEntityWithDuplicateFields_thenThrowsException() {
        CategoryEntity entity = CategoryEntity.builder().name(savedEntity.getName())
                .description(savedEntity.getDescription()).createdBy(savedEntity.getCreatedBy())
                .createdAt(savedEntity.getCreatedAt()).build();

        assertThrows(DataIntegrityViolationException.class, () -> repository.save(entity));
    }

    private void assertEqualsCategory(CategoryEntity expected, CategoryEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getCreatedBy(), actual.getCreatedBy());
    }

    @Test
    void givenMatchingName_whenFilterByNameContains_thenCategoriesAreFiltered() {
        assertEquals(3, repository.count());
        CategoryFilterOptions filterOptions = CategoryFilterOptions.builder().name(category1.getName()).build();
        Specification<CategoryEntity> specification = categorySpecification.getCategoriesByCriteria(filterOptions);
        List<CategoryEntity> categoryList = repository.findAll(specification);
        assertEquals(1, categoryList.size());
        assertEquals(category1.getName(), categoryList.get(0).getName());
    }

    @Test
    void givenNoMatchingName_whenFilterByNameContains_thenCategoriesAreNotFiltered() {
        assertEquals(3, repository.count());
        CategoryFilterOptions filterOptions = CategoryFilterOptions.builder().name("unknown").build();
        Specification<CategoryEntity> specification = categorySpecification.getCategoriesByCriteria(filterOptions);
        List<CategoryEntity> categoryList = repository.findAll(specification);
        assertEquals(0, categoryList.size());
    }

    @Test
    void givenMatchingParentName_whenFilterByParentNameContains_thenCategoriesAreFiltered() {
        assertEquals(3, repository.count());

        // Cat 1 is the parent to Cat 2
        CategoryFilterOptions filterOptions = CategoryFilterOptions.builder().parentName(category1.getName())
                .build();
        Specification<CategoryEntity> specification = categorySpecification.getCategoriesByCriteria(filterOptions);

        List<CategoryEntity> categoryList = repository.findAll(specification);
        assertEquals(1, categoryList.size());
        assertEquals(category2.getName(), categoryList.get(0).getName());
        assertEquals(category1.getName(), categoryList.get(0).getParentCategory().getName());
    }

    @Test
    void givenNoMatchingParentName_whenFilterByParentNameContains_thenCategoriesAreNotFiltered() {
        assertEquals(3, repository.count());
        CategoryFilterOptions filterOptions = CategoryFilterOptions.builder().parentName("unknown").build();
        Specification<CategoryEntity> specification = categorySpecification.getCategoriesByCriteria(filterOptions);
        List<CategoryEntity> categoryList = repository.findAll(specification);
        assertEquals(0, categoryList.size());
    }
}
