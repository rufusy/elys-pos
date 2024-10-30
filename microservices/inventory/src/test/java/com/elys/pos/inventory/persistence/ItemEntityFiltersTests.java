package com.elys.pos.inventory.persistence;

import com.elys.pos.inventory.repository.v1.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    }
}
