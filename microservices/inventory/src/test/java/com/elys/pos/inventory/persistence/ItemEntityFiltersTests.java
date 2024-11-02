package com.elys.pos.inventory.persistence;

import com.elys.pos.inventory.entity.v1.CategoryEntity;
import com.elys.pos.inventory.entity.v1.ItemEntity;
import com.elys.pos.inventory.entity.v1.ItemTypeEntity;
import com.elys.pos.inventory.entity.v1.StockTypeEntity;
import com.elys.pos.inventory.filter.v1.ItemFilterOptions;
import com.elys.pos.inventory.repository.v1.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(ItemSpecification.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ItemEntityFiltersTests extends PostgresTestBase {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ItemTypeRepository itemTypeRepository;
    @Autowired
    private StockTypeRepository stockTypeRepository;

    @Autowired
    private ItemSpecification itemSpecification;

    private ItemEntity item1, item2;
    private CategoryEntity category1, category2;
    private ItemTypeEntity itemType1, itemType2;
    private StockTypeEntity stockType1, stockType2;

    @BeforeEach
    void setupDb() {

        itemRepository.deleteAll();
        assertEquals(0, itemRepository.count());

        categoryRepository.deleteAll();
        assertEquals(0, categoryRepository.count());

        itemTypeRepository.deleteAll();
        assertEquals(0, itemTypeRepository.count());

        stockTypeRepository.deleteAll();
        assertEquals(0, stockTypeRepository.count());

        category1 = categoryRepository.save(CategoryEntity.builder().name("cat1").description("d1").createdBy(1L).createdAt(LocalDateTime.now()).build());
        category2 = categoryRepository.save(CategoryEntity.builder().name("cat2").description("d1").createdBy(1L).createdAt(LocalDateTime.now()).build());
        assertEquals(2, categoryRepository.count());

        itemType1 = itemTypeRepository.save(ItemTypeEntity.builder().name("Standard").createdAt(LocalDateTime.now()).createdBy(1L).build());
        itemType2 = itemTypeRepository.save(ItemTypeEntity.builder().name("Kit").createdAt(LocalDateTime.now()).createdBy(1L).build());
        assertEquals(2, itemTypeRepository.count());

        stockType1 = stockTypeRepository.save(StockTypeEntity.builder().name("Stocked").createdAt(LocalDateTime.now()).createdBy(1L).build());
        stockType2 = stockTypeRepository.save(StockTypeEntity.builder().name("Non Stocked").createdAt(LocalDateTime.now()).createdBy(1L).build());
        assertEquals(2, stockTypeRepository.count());

        item1 = itemRepository.save(ItemEntity.builder().name("item1").category(category1).description("d1")
                .sellingPrice(new BigDecimal("10.00")).hsnCode("96031000").itemNumber("123456").itemType(itemType1)
                .stockType(stockType1).serialized(false).batchTracked(false).createdAt(LocalDateTime.now()).createdBy(1L)
                .imageUrl("image_url_1").build());

        item2 = itemRepository.save(ItemEntity.builder().name("item2").category(category2).description("d1")
                .sellingPrice(new BigDecimal("1000.00")).hsnCode("68101990").itemNumber("654321").itemType(itemType2)
                .stockType(stockType2).serialized(true).batchTracked(true).createdAt(LocalDateTime.now()).createdBy(1L)
                .imageUrl("image_url_2").build());

        assertEquals(2, itemRepository.count());
    }

    @Test
    void givenMatchingName_whenFilterByNameContains_thenItemsAreFiltered() {
        assertEquals(2, itemRepository.count());
        ItemFilterOptions filterOptions = ItemFilterOptions.builder().name(item1.getName()).build();
        Specification<ItemEntity> specification = itemSpecification.getItemsByCriteria(filterOptions);
        List<ItemEntity> itemList = itemRepository.findAll(specification);
        assertEquals(1, itemList.size());
        assertEquals(item1.getName(), itemList.get(0).getName());
    }

    @Test
    void givenNoMatchingName_whenFilterByNameContains_thenItemsAreNotFiltered() {
        assertEquals(2, itemRepository.count());
        ItemFilterOptions filterOptions = ItemFilterOptions.builder().name("no matching name").build();
        Specification<ItemEntity> specification = itemSpecification.getItemsByCriteria(filterOptions);
        List<ItemEntity> itemList = itemRepository.findAll(specification);
        assertEquals(0, itemList.size());
    }

    @Test
    void givenMatchingHsnCode_whenFilterByHsnCodeContains_thenItemsAreFiltered() {
        assertEquals(2, itemRepository.count());
        ItemFilterOptions filterOptions = ItemFilterOptions.builder().hsnCode(item1.getHsnCode()).build();
        Specification<ItemEntity> specification = itemSpecification.getItemsByCriteria(filterOptions);
        List<ItemEntity> itemList = itemRepository.findAll(specification);
        assertEquals(1, itemList.size());
        assertEquals(item1.getHsnCode(), itemList.get(0).getHsnCode());
    }

    @Test
    void givenNoMatchingHsnCode_whenFilterByHsnCodeContains_thenItemsAreNotFiltered() {
        assertEquals(2, itemRepository.count());
        ItemFilterOptions filterOptions = ItemFilterOptions.builder().hsnCode("no matching hsn code").build();
        Specification<ItemEntity> specification = itemSpecification.getItemsByCriteria(filterOptions);
        List<ItemEntity> itemList = itemRepository.findAll(specification);
        assertEquals(0, itemList.size());
    }

    @Test
    void givenMatchingItemNumber_whenFilterByItemNumberContains_thenItemsAreFiltered() {
        assertEquals(2, itemRepository.count());
        ItemFilterOptions filterOptions = ItemFilterOptions.builder().itemNumber(item1.getItemNumber()).build();
        Specification<ItemEntity> specification = itemSpecification.getItemsByCriteria(filterOptions);
        List<ItemEntity> itemList = itemRepository.findAll(specification);
        assertEquals(1, itemList.size());
        assertEquals(item1.getItemNumber(), itemList.get(0).getItemNumber());
    }

    @Test
    void givenNoMatchingItemNumber_whenFilterByItemNumberContains_thenItemsAreNotFiltered() {
        assertEquals(2, itemRepository.count());
        ItemFilterOptions filterOptions = ItemFilterOptions.builder().itemNumber("no matching item number").build();
        Specification<ItemEntity> specification = itemSpecification.getItemsByCriteria(filterOptions);
        List<ItemEntity> itemList = itemRepository.findAll(specification);
        assertEquals(0, itemList.size());
    }

    @Test
    void givenMatchingCategoryName_whenFilterByCategoryNameContains_thenItemsAreFiltered() {
        assertEquals(2, itemRepository.count());
        ItemFilterOptions filterOptions = ItemFilterOptions.builder().categoryName(category1.getName()).build();
        Specification<ItemEntity> specification = itemSpecification.getItemsByCriteria(filterOptions);
        List<ItemEntity> itemList = itemRepository.findAll(specification);
        assertEquals(1, itemList.size());
        assertEquals(item1.getName(), itemList.get(0).getName());
        assertEquals(category1.getName(), itemList.get(0).getCategory().getName());
    }

    @Test
    void givenNoMatchingCategoryName_whenFilterByCategoryNameContains_thenItemsAreNotFiltered() {
        assertEquals(2, itemRepository.count());
        ItemFilterOptions filterOptions = ItemFilterOptions.builder().categoryName("no matching name").build();
        Specification<ItemEntity> specification = itemSpecification.getItemsByCriteria(filterOptions);
        List<ItemEntity> itemList = itemRepository.findAll(specification);
        assertEquals(0, itemList.size());
    }

    @Test
    void givenMatchingStockTypeName_whenFilterByStockTypeNameContains_thenItemsAreFiltered() {
        assertEquals(2, itemRepository.count());
        ItemFilterOptions filterOptions = ItemFilterOptions.builder().stockTypeName(stockType1.getName()).build();
        Specification<ItemEntity> specification = itemSpecification.getItemsByCriteria(filterOptions);
        List<ItemEntity> itemList = itemRepository.findAll(specification);
        assertEquals(1, itemList.size());
        assertEquals(item1.getName(), itemList.get(0).getName());
        assertEquals(stockType1.getName(), itemList.get(0).getStockType().getName());
    }

    @Test
    void givenNoMatchingStockTypeName_whenFilterByStockTypeNameContains_thenItemsAreNotFiltered() {
        assertEquals(2, itemRepository.count());
        ItemFilterOptions filterOptions = ItemFilterOptions.builder().stockTypeName("no matching name").build();
        Specification<ItemEntity> specification = itemSpecification.getItemsByCriteria(filterOptions);
        List<ItemEntity> itemList = itemRepository.findAll(specification);
        assertEquals(0, itemList.size());
    }

    @Test
    void givenMatchingItemTypeName_whenFilterByItemTypeNameContains_thenItemsAreFiltered() {
        assertEquals(2, itemRepository.count());
        ItemFilterOptions filterOptions = ItemFilterOptions.builder().itemTypeName(itemType2.getName()).build();
        Specification<ItemEntity> specification = itemSpecification.getItemsByCriteria(filterOptions);
        List<ItemEntity> itemList = itemRepository.findAll(specification);
        assertEquals(1, itemList.size());
        assertEquals(item2.getName(), itemList.get(0).getName());
        assertEquals(itemType2.getName(), itemList.get(0).getItemType().getName());
    }

    @Test
    void givenNoMatchingItemTypeName_whenFilterByItemTypeNameContains_thenItemsAreNotFiltered() {
        assertEquals(2, itemRepository.count());
        ItemFilterOptions filterOptions = ItemFilterOptions.builder().itemTypeName("no matching name").build();
        Specification<ItemEntity> specification = itemSpecification.getItemsByCriteria(filterOptions);
        List<ItemEntity> itemList = itemRepository.findAll(specification);
        assertEquals(0, itemList.size());
    }

    @Test
    void givenMatchingToAndFromCreatedAtDates_whenFilterByCreatedAtBetween_thenItemsAreFiltered() {
        LocalDate todayDate = LocalDate.now();
        LocalDate tomorrowDate = LocalDate.now().plusDays(1);
        ItemFilterOptions filterOptions = ItemFilterOptions.builder().createdAtLowerBound(todayDate)
                .createdAtUpperBound(tomorrowDate).build();
        Specification<ItemEntity> specification = itemSpecification.getItemsByCriteria(filterOptions);
        List<ItemEntity> itemList = itemRepository.findAll(specification);
        assertEquals(2, itemList.size());
    }

    @Test
    void givenNoMatchingToAndFromCreatedAtDates_whenFilterByCreatedAtBetween_thenItemsAreNotFiltered() {
        // Yesterday from start of day (yyyy-mm-ddT00:00) to end of day (yyyy-mm-ddT23:59:59.999999999)
        LocalDate yesterdayDate = LocalDate.now().minusDays(1);
        ItemFilterOptions filterOptions = ItemFilterOptions.builder().createdAtLowerBound(yesterdayDate)
                .createdAtUpperBound(yesterdayDate).build();
        Specification<ItemEntity> specification = itemSpecification.getItemsByCriteria(filterOptions);
        List<ItemEntity> itemList = itemRepository.findAll(specification);
        assertEquals(0, itemList.size());
    }

    @Test
    void givenMatchingToAndFromSellingPrices_whenFilterBySellingPriceBetween_thenItemsAreFiltered() {
        ItemFilterOptions filterOptions = ItemFilterOptions.builder().sellingPriceLowerBound(new BigDecimal("1.00"))
                .sellingPriceUpperBound(new BigDecimal("100.00")).build();
        Specification<ItemEntity> specification = itemSpecification.getItemsByCriteria(filterOptions);
        List<ItemEntity> itemList = itemRepository.findAll(specification);
        assertEquals(1, itemList.size());
    }

    @Test
    void givenNoMatchingToAndFromSellingPrices_whenFilterBySellingPriceBetween_thenItemsAreNotFiltered() {
        ItemFilterOptions filterOptions = ItemFilterOptions.builder().sellingPriceLowerBound(new BigDecimal("1.00"))
                .sellingPriceUpperBound(new BigDecimal("5.00")).build();
        Specification<ItemEntity> specification = itemSpecification.getItemsByCriteria(filterOptions);
        List<ItemEntity> itemList = itemRepository.findAll(specification);
        assertEquals(0, itemList.size());
    }

    @Test
    void givenSerializedFlagFalse_whenFilterBySerializedEquals_thenItemsAreFiltered() {
        assertEquals(2, itemRepository.count());
        ItemFilterOptions filterOptions = ItemFilterOptions.builder().serialized("isNotSerialized").build();
        Specification<ItemEntity> specification = itemSpecification.getItemsByCriteria(filterOptions);
        List<ItemEntity> itemList = itemRepository.findAll(specification);
        assertEquals(1, itemList.size());
        itemList.forEach(item -> System.out.println(item.getName() + " : " + item.isSerialized()));
        assertEquals(item1.getName(), itemList.get(0).getName());
        assertFalse(itemList.get(0).isSerialized());
    }

    @Test
    void givenBatchTrackedFlagFalse_whenFilterByBatchTrackedEquals_thenItemsAreFiltered() {
        assertEquals(2, itemRepository.count());
        ItemFilterOptions filterOptions = ItemFilterOptions.builder().batchTracked("isNotBatchTracked").build();
        Specification<ItemEntity> specification = itemSpecification.getItemsByCriteria(filterOptions);
        List<ItemEntity> itemList = itemRepository.findAll(specification);
        assertEquals(1, itemList.size());
        assertEquals(item1.getName(), itemList.get(0).getName());
        assertFalse(itemList.get(0).isBatchTracked());
    }

    @Test
    void givenSerializedFlagTrue_whenFilterBySerializedEquals_thenItemsAreFiltered() {
        assertEquals(2, itemRepository.count());
        ItemFilterOptions filterOptions = ItemFilterOptions.builder().serialized("isSerialized").build();
        Specification<ItemEntity> specification = itemSpecification.getItemsByCriteria(filterOptions);
        List<ItemEntity> itemList = itemRepository.findAll(specification);
        assertEquals(1, itemList.size());
        assertEquals(item2.getName(), itemList.get(0).getName());
        assertTrue(itemList.get(0).isSerialized());
    }

    @Test
    void givenBatchTrackedFlagTrue_whenFilterByBatchTrackedEquals_thenItemsAreFiltered() {
        assertEquals(2, itemRepository.count());
        ItemFilterOptions filterOptions = ItemFilterOptions.builder().batchTracked("isBatchTracked").build();
        Specification<ItemEntity> specification = itemSpecification.getItemsByCriteria(filterOptions);
        List<ItemEntity> itemList = itemRepository.findAll(specification);
        assertEquals(1, itemList.size());
        assertEquals(item2.getName(), itemList.get(0).getName());
        assertTrue(itemList.get(0).isBatchTracked());
    }

    @Test
    @Rollback
    void givenMatchingToAndFromUpdatedAtDates_whenFilterByUpdatedAtBetween_thenItemsAreFiltered() {
        // Update item with today's date
        ItemEntity foundItem = itemRepository.findById(item1.getId()).orElse(null);
        assertNotNull(foundItem);
        foundItem.setName("new name");
        foundItem.setUpdatedAt(LocalDateTime.now());
        itemRepository.save(foundItem);

        LocalDate todayDate = LocalDate.now();
        ItemFilterOptions filterOptions = ItemFilterOptions.builder().updatedAtLowerBound(todayDate) // From start of day
                .updatedAtUpperBound(todayDate) // Until end of day
                .build();
        Specification<ItemEntity> specification = itemSpecification.getItemsByCriteria(filterOptions);
        List<ItemEntity> itemList = itemRepository.findAll(specification);
        assertEquals(1, itemList.size());
        assertEquals("new name", itemList.get(0).getName());
    }

    @Test
    @Rollback
    void givenNoMatchingToAndFromUpdatedAtDates_whenFilterByUpdatedAtBetween_thenItemsAreNotFiltered() {
        // Update item with today's date
        ItemEntity foundItem = itemRepository.findById(item1.getId()).orElse(null);
        assertNotNull(foundItem);
        foundItem.setName("new name");
        foundItem.setUpdatedAt(LocalDateTime.now());
        itemRepository.save(foundItem);

        // Yesterday from start of day (yyyy-mm-ddT00:00) to end of day (yyyy-mm-ddT23:59:59.999999999)
        LocalDate yesterdayDate = LocalDate.now().minusDays(1);
        ItemFilterOptions filterOptions = ItemFilterOptions.builder().updatedAtLowerBound(yesterdayDate)
                .updatedAtUpperBound(yesterdayDate).build();
        Specification<ItemEntity> specification = itemSpecification.getItemsByCriteria(filterOptions);
        List<ItemEntity> itemList = itemRepository.findAll(specification);
        assertEquals(0, itemList.size());
    }

    @Test
    @Rollback
    void givenMatchingToAndFromDeletedAtDates_whenFilterByDeletedAtBetween_thenItemsAreFiltered() {
        // Delete item with today's date
        ItemEntity foundItem = itemRepository.findById(item1.getId()).orElse(null);
        assertNotNull(foundItem);
        foundItem.setDeletedAt(LocalDateTime.now());
        itemRepository.save(foundItem);

        LocalDate todayDate = LocalDate.now();
        ItemFilterOptions filterOptions = ItemFilterOptions.builder().deletedAtLowerBound(todayDate)
                .deletedAtUpperBound(todayDate).build();
        Specification<ItemEntity> specification = itemSpecification.getItemsByCriteria(filterOptions);
        List<ItemEntity> itemList = itemRepository.findAll(specification);
        assertEquals(1, itemList.size());
        assertEquals(item1.getName(), itemList.get(0).getName());
    }

    @Test
    @Rollback
    void givenNoMatchingToAndFromDeletedAtDates_whenFilterByDeletedAtBetween_thenItemsAreNotFiltered() {
        // Delete item with today's date
        ItemEntity foundItem = itemRepository.findById(item1.getId()).orElse(null);
        assertNotNull(foundItem);
        foundItem.setDeletedAt(LocalDateTime.now());
        itemRepository.save(foundItem);

        // Yesterday from start of day (yyyy-mm-ddT00:00) to end of day (yyyy-mm-ddT23:59:59.999999999)
        LocalDate yesterdayDate = LocalDate.now().minusDays(1);
        ItemFilterOptions filterOptions = ItemFilterOptions.builder().deletedAtLowerBound(yesterdayDate)
                .deletedAtUpperBound(yesterdayDate).build();
        Specification<ItemEntity> specification = itemSpecification.getItemsByCriteria(filterOptions);
        List<ItemEntity> itemList = itemRepository.findAll(specification);
        assertEquals(0, itemList.size());
    }

    @Test
    @Rollback
    void givenDeletedFlags_whenFilterByDeletedEquals_thenItemsAreFiltered() {
        // Delete item 1
        ItemEntity foundItem = itemRepository.findById(item1.getId()).orElse(null);
        assertNotNull(foundItem);
        foundItem.setDeleted(true);
        itemRepository.save(foundItem);

        ItemFilterOptions filterOptions = ItemFilterOptions.builder().deleted("isDeleted").build();
        Specification<ItemEntity> specification = itemSpecification.getItemsByCriteria(filterOptions);
        List<ItemEntity> deletedItemList = itemRepository.findAll(specification);
        // List should now contain only item 1
        assertEquals(1, deletedItemList.size());
        assertEquals(item1.getName(), deletedItemList.get(0).getName());
        assertNotEquals(item2.getName(), deletedItemList.get(0).getName());

        filterOptions = ItemFilterOptions.builder().deleted("isNotDeleted").build();
        specification = itemSpecification.getItemsByCriteria(filterOptions);
        List<ItemEntity> activeItemList = itemRepository.findAll(specification);
        // List should now contain only item 2
        assertEquals(1, activeItemList.size());
        assertEquals(item2.getName(), activeItemList.get(0).getName());
        assertNotEquals(item1.getName(), activeItemList.get(0).getName());
    }
}
