package com.elys.pos.inventory.persistence;

import com.elys.pos.inventory.entity.v1.CategoryEntity;
import com.elys.pos.inventory.repository.v1.CategoryRepository;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryTests extends PostgresTestBase {

    @Autowired
    private CategoryRepository repository;

    private CategoryEntity savedEntity;

    @BeforeEach
    void setupDb() {
        repository.deleteAll();
        assertEquals(0, repository.count());

        CategoryEntity entity = CategoryEntity.builder()
                .name("n1")
                .description("d1")
                .createdBy(1L)
                .createdAt(LocalDateTime.now())
                .build();
        savedEntity = repository.save(entity);

        assertEquals(0, savedEntity.getVersion());
        assertNull(savedEntity.getUpdatedAt());
        assertNull(savedEntity.getUpdatedBy());
        assertNull(savedEntity.getDeletedBy());
        assertNull(savedEntity.getDeletedAt());
        assertFalse(savedEntity.isDeleted());
        assertEqualsCategory(entity, savedEntity);
    }

    @Test
    void create() {
        CategoryEntity entity = CategoryEntity.builder()
                .name("n2")
                .description("d2")
                .parentCategory(savedEntity)
                .createdBy(1L)
                .createdAt(LocalDateTime.now())
                .build();
        repository.save(entity);

        CategoryEntity foundEntity = repository.findById(entity.getId()).orElse(null);
        assertEquals(2, repository.count());
        assert foundEntity != null;
        assertEquals(savedEntity.getId(), foundEntity.getParentCategory().getId()); // verify parent/child relationship
        assertEqualsCategory(entity, foundEntity);
    }

    @Test
    void update() {
        savedEntity.setName("n1 updated");
        savedEntity.setDescription("d1 updated");
        savedEntity.setUpdatedBy(2L);
        savedEntity.setUpdatedAt(LocalDateTime.now());
        repository.save(savedEntity);

        CategoryEntity foundEntity = repository.findById(savedEntity.getId()).get();

        // verify the new props
        assertEquals(1, foundEntity.getVersion());
        assertEquals("n1 updated", foundEntity.getName());
        assertEquals("d1 updated", foundEntity.getDescription());
        assertEquals(2L, foundEntity.getUpdatedBy());
        assertNotNull(foundEntity.getUpdatedAt());
    }

    @Test
    void softDelete() {
        repository.softDelete(savedEntity.getId(), 2L, LocalDateTime.now());

        // verify that category was not removed from db
        CategoryEntity foundEntity = repository.findById(savedEntity.getId()).orElse(null);
        assertNotNull(foundEntity);

        // verify soft delete fields
        assertTrue(foundEntity.isDeleted());
        assertEquals(2L, foundEntity.getDeletedBy());
        assertNotNull(foundEntity.getDeletedAt());
    }

    @Test
    void findAllActive() {
        CategoryEntity deletedCategory = CategoryEntity.builder()
                .name("Deleted Category")
                .description("A deleted category")
                .createdBy(1L)
                .createdAt(LocalDateTime.now())
                .deleted(true)
                .build();
        repository.save(deletedCategory);

        // db should have 2 categories total
        assertEquals(2, repository.count());

        // Check that only one active category is returned
        List<CategoryEntity> activeCategories = repository.findAllActive();
        assertEquals(1, activeCategories.size());
        assertFalse(activeCategories.get(0).isDeleted());
        assertEqualsCategory(savedEntity, activeCategories.get(0));
    }

    @Test
    void lazyFetching() {
        // Fetch the category without accessing items
        CategoryEntity foundEntity = repository.findById(savedEntity.getId()).orElse(null);
        assertNotNull(foundEntity);

        // Try accessing items or kits after the session has been closed,
        // This should throw an exception
        assertThrows(LazyInitializationException.class, () -> {
            assertThat(foundEntity.getItems(), hasSize(0));
            assertThat(foundEntity.getKits(), hasSize(0));
        });
    }

    @Test
    void eagerFetching() {
        CategoryEntity foundEntity = repository.findById(savedEntity.getId()).orElse(null);
        assertNotNull(foundEntity);
        assertThat(foundEntity.getAttributeLinks(), hasSize(0));
    }

    @Test
    void optimisticLockeError() {
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
    void duplicateError() {
        CategoryEntity entity = CategoryEntity.builder()
                .name(savedEntity.getName()).description(savedEntity.getDescription())
                .createdBy(savedEntity.getCreatedBy()).createdAt(savedEntity.getCreatedAt())
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> repository.save(entity));
    }

    private void assertEqualsCategory(CategoryEntity expected, CategoryEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getCreatedBy(), actual.getCreatedBy());
    }
}
