package com.elys.pos.inventory.persistence;

import com.elys.pos.inventory.v1.entity.CategoryEntity;
import com.elys.pos.inventory.v1.entity.ItemEntity;
import com.elys.pos.inventory.v1.entity.ItemTypeEntity;
import com.elys.pos.inventory.v1.entity.StockTypeEntity;
import com.elys.pos.inventory.v1.repository.CategoryRepository;
import com.elys.pos.inventory.v1.repository.ItemRepository;
import com.elys.pos.inventory.v1.repository.ItemTypeRepository;
import com.elys.pos.inventory.v1.repository.StockTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ItemEntityTests extends PostgresTestBase {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ItemTypeRepository itemTypeRepository;
    @Autowired
    private StockTypeRepository stockTypeRepository;

    private ItemEntity savedItem;
    private CategoryEntity savedCategory;
    private ItemTypeEntity savedItemType;
    private StockTypeEntity savedStockType;

//    @BeforeEach
//    void setupDb() {
//        itemRepository.deleteAll();
//        assertEquals(0, itemRepository.count());
//
//        categoryRepository.deleteAll();
//        assertEquals(0, categoryRepository.count());
//
//        itemTypeRepository.deleteAll();
//        assertEquals(0, itemTypeRepository.count());
//
//        stockTypeRepository.deleteAll();
//        assertEquals(0, stockTypeRepository.count());
//
//        CategoryEntity category = CategoryEntity.builder().name("n1").description("d1").createdBy(1L)
//                .createdAt(LocalDateTime.now()).build();
//        savedCategory = categoryRepository.save(category);
//
//        ItemTypeEntity itemType = ItemTypeEntity.builder().name("Standard").createdAt(LocalDateTime.now())
//                .createdBy(1L).build();
//        savedItemType = itemTypeRepository.save(itemType);
//
//        StockTypeEntity stockType = StockTypeEntity.builder().name("Stocked").createdAt(LocalDateTime.now())
//                .createdBy(1L).build();
//        savedStockType = stockTypeRepository.save(stockType);
//
//        // Populate all non-nullable fields
//        ItemEntity entity = ItemEntity.builder()
//                .name("n").category(savedCategory).description("d").sellingPrice(new BigDecimal("1.00")).hsnCode("123")
//                .itemType(savedItemType).stockType(savedStockType).serialized(false).batchTracked(false)
//                .createdAt(LocalDateTime.now()).createdBy(1L)
//                .build();
//
//        savedItem = itemRepository.save(entity);
//        assertEqualsItem(entity, savedItem);
//        assertFalse(savedItem.isSerialized());
//        assertFalse(savedItem.isBatchTracked());
//
//        assertEquals(0, savedItem.getVersion());
//        assertNotNull(savedItem.getCreatedBy());
//        assertNotNull(savedItem.getCreatedAt());
//        assertNull(savedItem.getUpdatedAt());
//        assertNull(savedItem.getUpdatedBy());
//        assertNull(savedItem.getDeletedBy());
//        assertNull(savedItem.getDeletedAt());
//        assertFalse(savedItem.isDeleted());
//    }

//    @Test
//    void whenSavingItemEntityWithNoValidationErrors_thenSuccess() {
//        ItemEntity entity = ItemEntity.builder()
//                .name("n2").category(savedCategory).description("d2").sellingPrice(new BigDecimal("1.00")).hsnCode("123")
//                .itemType(savedItemType).stockType(savedStockType).serialized(false).batchTracked(false)
//                .createdAt(LocalDateTime.now()).createdBy(1L)
//                .build();
//
//        savedItem = itemRepository.save(entity);
//
//        ItemEntity foundItem = itemRepository.findById(entity.getId()).orElse(null);
//        assertEquals(2, itemRepository.count());
//        assertEqualsItem(entity, savedItem);
//    }

//    @Test
//    void whenSavingItemEntityWithValidationErrors_thenThrowsException() {
//        ItemEntity entity = ItemEntity.builder()
//                .name("n2").category(null).description("d2").sellingPrice(new BigDecimal("1.00")).hsnCode("123")
//                .itemType(null).stockType(null).serialized(false).batchTracked(false)
//                .createdAt(LocalDateTime.now()).createdBy(1L)
//                .build();
//
//        assertThrows(TransactionSystemException.class, () -> itemRepository.save(entity));
//    }

//    @Test
//    void whenUpdatingItemEntityWithNoValidationErrors_thenSuccess() {
//        savedItem.setName("new name");
//        savedItem.setDescription("new desc");
//        savedItem.setUpdatedBy(2L);
//        savedItem.setUpdatedAt(LocalDateTime.now());
//        itemRepository.save(savedItem);
//
//        ItemEntity foundEntity = itemRepository.findById(savedItem.getId()).get();
//
//        assertEquals(1, foundEntity.getVersion());
//        assertEquals("new name", foundEntity.getName());
//        assertEquals("new desc", foundEntity.getDescription());
//        assertEquals(2L, foundEntity.getUpdatedBy());
//        assertNotNull(foundEntity.getUpdatedAt());
//    }

//    @Test
//    void whenSoftDeletingAnItemEntity_thenItIsOnlyMarkedAsDeletedButRetained() {
//        itemRepository.softDelete(savedItem.getId(), 2L, LocalDateTime.now());
//
//        ItemEntity foundEntity = itemRepository.findById(savedItem.getId()).get();
//
//        assertTrue(foundEntity.isDeleted());
//        assertEquals(2L, foundEntity.getDeletedBy());
//        assertNotNull(foundEntity.getDeletedAt());
//    }

//    @Test
//    void whenActiveItemsAreRetrieved_thenReturnItemsNotDeleted() {
//
//        assertEquals(1, itemRepository.findAllActive().size());
//
//        itemRepository.softDelete(savedItem.getId(), 1L, LocalDateTime.now());
//
//        assertEquals(0, itemRepository.findAllActive().size());
//    }

//    @Test
//    void whenAccessingLazyRelationsOutOfSession_thenThrowsException() {
//        ItemEntity foundEntity = itemRepository.findById(savedItem.getId()).orElse(null);
//        assertNotNull(foundEntity);
//
//        assertThrows(LazyInitializationException.class, () -> assertThat(foundEntity.getKits(), hasSize(0)));
//        assertThrows(LazyInitializationException.class, () -> assertThat(foundEntity.getReceived(), hasSize(0)));
//        assertThrows(LazyInitializationException.class, () -> assertThat(foundEntity.getTags(), hasSize(0)));
//    }

//    @Test
//    void whenAccessingEagerRelations_thenSuccess() {
//        ItemEntity foundEntity = itemRepository.findById(savedItem.getId()).orElse(null);
//        assertNotNull(foundEntity);
//
//        assertNotNull(foundEntity.getCategory());
//        assertEquals(savedCategory.getId(), foundEntity.getCategory().getId());
//
//        assertNotNull(foundEntity.getItemType());
//        assertEquals(savedItemType.getId(), foundEntity.getItemType().getId());
//
//        assertNotNull(foundEntity.getStockType());
//        assertEquals(savedStockType.getId(), foundEntity.getStockType().getId());
//
//        assertThat(foundEntity.getAttributeLinks(), hasSize(0));
//    }

//    @Test
//    void whenUpdatingItemEntityWithLowerVersion_thenThrowsException() {
//        // Store the saved entity in two separate entity objects
//        ItemEntity entity1 = itemRepository.findById(savedItem.getId()).get();
//        ItemEntity entity2 = itemRepository.findById(savedItem.getId()).get();
//
//        // Update the entity using the first entity object
//        entity1.setName("name updated");
//        itemRepository.save(entity1);
//
//        // Update the entity using the second entity object
//        // This should fail since the second entity now holds an old version number, i.e. an Optimistic Lock Error
//        assertThrows(OptimisticLockingFailureException.class, () -> {
//            entity2.setName("name updated");
//            itemRepository.save(entity2);
//        });
//
//        ItemEntity updatedEntity = itemRepository.findById(savedItem.getId()).get();
//        assertEquals(1, updatedEntity.getVersion());
//        assertEquals("name updated", updatedEntity.getName());
//    }

//    @Test
//    void whenSavingItemEntityWithDuplicateFields_thenThrowsException() {
//        ItemEntity entity = ItemEntity.builder()
//                .name(savedItem.getName()) // should be unique
//                .category(savedCategory)
//                .description(savedItem.getDescription()) // should be unique
//                .sellingPrice(new BigDecimal("1.00")).hsnCode("123")
//                .itemType(savedItemType).stockType(savedStockType).serialized(false).batchTracked(false)
//                .createdAt(LocalDateTime.now()).createdBy(1L)
//                .build();
//
//        assertThrows(DataIntegrityViolationException.class, () -> itemRepository.save(entity));
//    }

//    private void assertEqualsItem(ItemEntity expected, ItemEntity actual) {
//        assertEquals(expected.getId(), actual.getId());
//        assertEquals(expected.getName(), actual.getName());
//        assertEquals(expected.getCategory().getId(), actual.getCategory().getId());
//        assertEquals(expected.getDescription(), actual.getDescription());
//        assertEquals(expected.getSellingPrice(), actual.getSellingPrice());
//        assertEquals(expected.getHsnCode(), actual.getHsnCode());
//        assertEquals(expected.getItemType(), actual.getItemType());
//        assertEquals(expected.getStockType(), actual.getStockType());
//    }
}
