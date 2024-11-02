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

        CategoryEntity category1 = categoryRepository.save(
                CategoryEntity.builder().name("cat1").description("d1").createdBy(1L)
                        .createdAt(LocalDateTime.now()).build()
        );

        ItemTypeEntity itemType1 = itemTypeRepository.save(
                ItemTypeEntity.builder().name("Standard").createdAt(LocalDateTime.now()).createdBy(1L).build()
        );

        StockTypeEntity stockType1 = stockTypeRepository.save(
                StockTypeEntity.builder().name("Stocked").createdAt(LocalDateTime.now()).createdBy(1L).build()
        );

        item1 = itemRepository.save(
                ItemEntity.builder().name("item1").category(category1).description("d1")
                        .sellingPrice(new BigDecimal("10.00")).hsnCode("123").itemType(itemType1).stockType(stockType1)
                        .serialized(false).batchTracked(false).createdAt(LocalDateTime.now()).createdBy(1L).build()
        );

        item2 = itemRepository.save(
                ItemEntity.builder().name("item2").category(category1).description("d1")
                        .sellingPrice(new BigDecimal("1000.00")).hsnCode("123").itemType(itemType1).stockType(stockType1)
                        .serialized(true).batchTracked(true).createdAt(LocalDateTime.now()).createdBy(1L).build()
        );
    }

    @Test
    void givenMatchingName_whenFilterByNameContains_thenItemsFiltered() {
        ItemFilterOptions filterOptions = ItemFilterOptions.builder().name(item1.getName()).build();
        Specification<ItemEntity> specification = itemSpecification.getItemsByCriteria(filterOptions);
        List<ItemEntity> itemList = itemRepository.findAll(specification);
        assertEquals(1, itemList.size());
        assertEquals(item1.getName(), itemList.get(0).getName());
    }

    @Test
    void givenNoMatchingName_whenFilterByNameContains_thenItemsAreNotFiltered() {
        ItemFilterOptions filterOptions = ItemFilterOptions.builder().name("no matching name").build();
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
}
