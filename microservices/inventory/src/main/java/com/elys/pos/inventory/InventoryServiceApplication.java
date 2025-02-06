package com.elys.pos.inventory;

import com.elys.pos.inventory.v1.entity.CategoryEntity;
import com.elys.pos.inventory.v1.entity.ItemEntity;
import com.elys.pos.inventory.v1.entity.ItemTypeEntity;
import com.elys.pos.inventory.v1.entity.StockTypeEntity;
import com.elys.pos.inventory.v1.repository.CategoryRepository;
import com.elys.pos.inventory.v1.repository.ItemRepository;
import com.elys.pos.inventory.v1.repository.ItemTypeRepository;
import com.elys.pos.inventory.v1.repository.StockTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = {"com.elys.pos.inventory", "com.elys.pos.util.v1"})
public class InventoryServiceApplication {
    private final Integer threadPoolSize;
    private final Integer taskQueueSize;

    public InventoryServiceApplication(@Value("${app.threadPoolSize:10}") Integer threadPoolSize,
                                       @Value("${app.taskQueueSize:100}") Integer taskQueueSize) {
        this.threadPoolSize = threadPoolSize;
        this.taskQueueSize = taskQueueSize;
    }

    @Bean(name = "jdbcScheduler")
    public Scheduler jdbcScheduler() {
        log.info("Creates a jdbcScheduler with the thread pool size = {}", threadPoolSize);
        return Schedulers.newBoundedElastic(threadPoolSize, taskQueueSize, "jdbc-pool");
    }

    @Bean(name = "publishEventScheduler")
    public Scheduler publishEventScheduler() {
        log.info("Creates a messagingScheduler with connectionPoolSize = {}", threadPoolSize);
        return Schedulers.newBoundedElastic(threadPoolSize, taskQueueSize, "publish-pool");
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(InventoryServiceApplication.class, args);
        String postgresUrl = ctx.getEnvironment().getProperty("spring.datasource.url");
        log.info("Connected to Postgres: {}", postgresUrl);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ItemRepository itemRepository, CategoryRepository categoryRepository,
                                               ItemTypeRepository itemTypeRepository, StockTypeRepository stockTypeRepository) {
        return args -> {

            String testUser1 = "test_user1";

            CategoryEntity category1 = categoryRepository.save(CategoryEntity.builder().name("cat1").description("d1")
                    .createdBy(testUser1).createdAt(LocalDateTime.now()).build());
            CategoryEntity category2 = categoryRepository.save(CategoryEntity.builder().name("cat2").description("d1")
                    .createdBy(testUser1).createdAt(LocalDateTime.now()).build());

            ItemTypeEntity itemType1 = itemTypeRepository.save(ItemTypeEntity.builder().name("Standard")
                    .createdAt(LocalDateTime.now()).createdBy(testUser1).build());
            ItemTypeEntity itemType2 = itemTypeRepository.save(ItemTypeEntity.builder().name("Kit")
                    .createdAt(LocalDateTime.now()).createdBy(testUser1).build());

            StockTypeEntity stockType1 = stockTypeRepository.save(StockTypeEntity.builder().name("Stocked")
                    .createdAt(LocalDateTime.now()).createdBy(testUser1).build());
            StockTypeEntity stockType2 = stockTypeRepository.save(StockTypeEntity.builder().name("Non Stocked")
                    .createdAt(LocalDateTime.now()).createdBy(testUser1).build());

            ItemEntity item1 = itemRepository.save(ItemEntity.builder().name("item1").category(category1).description("d1")
                    .sellingPrice(new BigDecimal("10.00")).hsnCode("96031000").itemNumber("123456").itemType(itemType1)
                    .stockType(stockType1).serialized(false).batchTracked(false).createdAt(LocalDateTime.now())
                    .createdBy(testUser1).imageUrl("image_url_1").supplierId(UUID.randomUUID())
                    .taxCategoryId(UUID.randomUUID()).build());

            ItemEntity item2 = itemRepository.save(ItemEntity.builder().name("item2").category(category2).description("d1")
                    .sellingPrice(new BigDecimal("1000.00")).hsnCode("68101990").itemNumber("654321").itemType(itemType2)
                    .stockType(stockType2).serialized(true).batchTracked(true).createdAt(LocalDateTime.now())
                    .createdBy(testUser1).imageUrl("image_url_2").supplierId(UUID.randomUUID())
                    .taxCategoryId(UUID.randomUUID()).build());

            List<ItemEntity> itemEntityList = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                itemEntityList.add(
                        ItemEntity.builder().name("item" + i + 3).category(category2).description("d1")
                                .sellingPrice(new BigDecimal("1000.00")).hsnCode("68101990").itemNumber("654321" + i + 3).itemType(itemType2)
                                .stockType(stockType2).serialized(true).batchTracked(true).createdAt(LocalDateTime.now())
                                .createdBy(testUser1).imageUrl("image_url_2").supplierId(UUID.randomUUID())
                                .taxCategoryId(UUID.randomUUID()).build()
                );
            }
            itemRepository.saveAll(itemEntityList);
        };
    }
}
