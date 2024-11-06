package com.elys.pos.inventory;

import com.elys.pos.inventory.v1.entity.CategoryEntity;
import com.elys.pos.inventory.v1.entity.ItemEntity;
import com.elys.pos.inventory.v1.entity.ItemTypeEntity;
import com.elys.pos.inventory.v1.entity.StockTypeEntity;
import com.elys.pos.inventory.v1.repository.CategoryRepository;
import com.elys.pos.inventory.v1.repository.ItemRepository;
import com.elys.pos.inventory.v1.repository.ItemTypeRepository;
import com.elys.pos.inventory.v1.repository.StockTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ItemRepository itemRepository, CategoryRepository categoryRepository,
                                               ItemTypeRepository itemTypeRepository, StockTypeRepository stockTypeRepository) {
        return args -> {

            UUID user1Id = UUID.randomUUID();

            CategoryEntity category1 = categoryRepository.save(CategoryEntity.builder().name("cat1").description("d1")
                    .createdBy(user1Id).createdAt(LocalDateTime.now()).build());
            CategoryEntity category2 = categoryRepository.save(CategoryEntity.builder().name("cat2").description("d1")
                    .createdBy(user1Id).createdAt(LocalDateTime.now()).build());

            ItemTypeEntity itemType1 = itemTypeRepository.save(ItemTypeEntity.builder().name("Standard")
                    .createdAt(LocalDateTime.now()).createdBy(user1Id).build());
            ItemTypeEntity itemType2 = itemTypeRepository.save(ItemTypeEntity.builder().name("Kit")
                    .createdAt(LocalDateTime.now()).createdBy(user1Id).build());

            StockTypeEntity stockType1 = stockTypeRepository.save(StockTypeEntity.builder().name("Stocked")
                    .createdAt(LocalDateTime.now()).createdBy(user1Id).build());
            StockTypeEntity stockType2 = stockTypeRepository.save(StockTypeEntity.builder().name("Non Stocked")
                    .createdAt(LocalDateTime.now()).createdBy(user1Id).build());

            ItemEntity item1 = itemRepository.save(ItemEntity.builder().name("item1").category(category1).description("d1")
                    .sellingPrice(new BigDecimal("10.00")).hsnCode("96031000").itemNumber("123456").itemType(itemType1)
                    .stockType(stockType1).serialized(false).batchTracked(false).createdAt(LocalDateTime.now())
                    .createdBy(UUID.randomUUID()).imageUrl("image_url_1").supplierId(UUID.randomUUID())
                    .taxCategoryId(UUID.randomUUID()).build());

            ItemEntity item2 = itemRepository.save(ItemEntity.builder().name("item2").category(category2).description("d1")
                    .sellingPrice(new BigDecimal("1000.00")).hsnCode("68101990").itemNumber("654321").itemType(itemType2)
                    .stockType(stockType2).serialized(true).batchTracked(true).createdAt(LocalDateTime.now())
                    .createdBy(UUID.randomUUID()).imageUrl("image_url_2").supplierId(UUID.randomUUID())
                    .taxCategoryId(UUID.randomUUID()).build());

            List<ItemEntity> itemEntityList = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                itemEntityList.add(
                        ItemEntity.builder().name("item" + i + 3).category(category2).description("d1")
                                .sellingPrice(new BigDecimal("1000.00")).hsnCode("68101990").itemNumber("654321" + i + 3).itemType(itemType2)
                                .stockType(stockType2).serialized(true).batchTracked(true).createdAt(LocalDateTime.now())
                                .createdBy(UUID.randomUUID()).imageUrl("image_url_2").supplierId(UUID.randomUUID())
                                .taxCategoryId(UUID.randomUUID()).build()
                );
            }
            itemRepository.saveAll(itemEntityList);
        };
    }

}
