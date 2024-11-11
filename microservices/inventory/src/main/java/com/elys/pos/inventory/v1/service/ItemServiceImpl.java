package com.elys.pos.inventory.v1.service;

import com.elys.pos.inventory.v1.api.model.Item;
import com.elys.pos.inventory.v1.entity.CategoryEntity;
import com.elys.pos.inventory.v1.entity.ItemEntity;
import com.elys.pos.inventory.v1.entity.ItemTypeEntity;
import com.elys.pos.inventory.v1.entity.StockTypeEntity;
import com.elys.pos.inventory.v1.mapper.ItemMapper;
import com.elys.pos.inventory.v1.repository.CategoryRepository;
import com.elys.pos.inventory.v1.repository.ItemRepository;
import com.elys.pos.inventory.v1.repository.ItemTypeRepository;
import com.elys.pos.inventory.v1.repository.StockTypeRepository;
import com.elys.pos.inventory.v1.specification.SpecificationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final StockTypeRepository stockTypeRepository;
    private final ItemTypeRepository itemTypeRepository;

    private final ItemMapper itemMapper;

    @Qualifier("jdbcScheduler")
    private final Scheduler jdbcScheduler;

    @Qualifier("publishEventScheduler")
    private final Scheduler publishEventScheduler;

    public ItemServiceImpl(ItemRepository itemRepository, CategoryRepository categoryRepository,
                           StockTypeRepository stockTypeRepository, ItemTypeRepository itemTypeRepository,
                           ItemMapper itemMapper, Scheduler jdbcScheduler, Scheduler publishEventScheduler) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.stockTypeRepository = stockTypeRepository;
        this.itemTypeRepository = itemTypeRepository;
        this.itemMapper = itemMapper;
        this.jdbcScheduler = jdbcScheduler;
        this.publishEventScheduler = publishEventScheduler;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Item> getItems(Specification<ItemEntity> specification, Pageable pageable) {
        Specification<ItemEntity> combinedSpec = specification
                .and(SpecificationUtils.booleanFieldEquals("deleted", false));
        return itemRepository.findAll(combinedSpec, pageable).map(itemMapper::entityToApi);
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<Item> getItemById(String itemId) {
        return Mono.fromCallable(() -> internalGetItemById(itemId)).subscribeOn(jdbcScheduler);
    }

    private Item internalGetItemById(String itemId) {
        log.debug("getItem: will try to get an item of id: {}", itemId);
        ItemEntity entity = itemRepository.findById(UUID.fromString(itemId))
                .orElseThrow(() -> new RuntimeException("No item found for itemId: " + itemId));
        return itemMapper.entityToApi(entity);
    }

    @Transactional
    @Override
    public Mono<Item> createItem(Item body) {
        return Mono.fromCallable(() -> internalCreateItem(body)).subscribeOn(jdbcScheduler);
    }

    private Item internalCreateItem(Item body) {
        log.debug("createItem: will try to create an item of name: {}", body.getName());

        // org.springframework.dao.DataIntegrityViolationException: could not execute statement [ERROR: duplicate key value violates unique constraint "items_item_number_key"
        ItemEntity itemEntity = itemMapper.apiToEntity(body);
        itemEntity.setCreatedAt(LocalDateTime.now());

        itemEntity.setCategory(this.getCategory(body.getCategory().getName()));
        itemEntity.setItemType(this.getItemType(body.getItemType().getName()));
        itemEntity.setStockType(this.getStockType(body.getStockType().getName()));
        ItemEntity newItemEntity = itemRepository.save(itemEntity);

        log.debug("createItem: created an item entity of name: {}", body.getName());

        return itemMapper.entityToApi(newItemEntity);
    }

    @Transactional
    @Override
    public Mono<Item> updateItem(Item body) {
        return Mono.fromCallable(() -> internalUpdateItem(body)).subscribeOn(jdbcScheduler);
    }

    private Item internalUpdateItem(Item body) {
        log.debug("updateItem: will try to update an item of name: {}", body.getName());

        ItemEntity existingItemEntity = itemRepository.findById(body.getId())
                .orElseThrow(() -> new RuntimeException("updateItem: no Item found with id: " + body.getId()));
// Caused by: jakarta.validation.ConstraintViolationException: Validation failed for classes [com.elys.pos.inventory.v1.entity.ItemEntity]
//        itemMapper.updateEntityFromApi(body, existingItemEntity);

        ItemEntity itemEntity = itemMapper.apiToEntity(body);

        existingItemEntity.setName(itemEntity.getName());
        existingItemEntity.setDescription(itemEntity.getDescription());
        existingItemEntity.setItemNumber(itemEntity.getItemNumber());
        existingItemEntity.setImageUrl(itemEntity.getImageUrl());
        existingItemEntity.setSupplierId(itemEntity.getSupplierId());
        existingItemEntity.setTaxCategoryId(itemEntity.getTaxCategoryId());
        existingItemEntity.setHsnCode(itemEntity.getHsnCode());
        existingItemEntity.setSellingPrice(itemEntity.getSellingPrice());
        existingItemEntity.setSerialized(itemEntity.isSerialized());
        existingItemEntity.setBatchTracked(itemEntity.isBatchTracked());
        existingItemEntity.setUpdatedBy(itemEntity.getUpdatedBy());
        existingItemEntity.setUpdatedAt(LocalDateTime.now());
        existingItemEntity.setCategory(this.getCategory(body.getCategory().getName()));
        existingItemEntity.setItemType(this.getItemType(body.getItemType().getName()));
        existingItemEntity.setStockType(this.getStockType(body.getStockType().getName()));

        ItemEntity updatedItem = itemRepository.save(existingItemEntity);
        log.debug("updateItem: updated an item entity of name: {}", existingItemEntity.getName());

        return itemMapper.entityToApi(updatedItem);
    }

    @Transactional
    @Override
    public Mono<Void> deleteItem(String itemId) {
        return Mono.fromRunnable(() -> internalDeleteItem(itemId)).subscribeOn(jdbcScheduler).then();
    }

    private void internalDeleteItem(String itemId) {
        log.debug("deleteItem: will try to delete an item with id: {}", itemId);

        if (!isValidUUID(itemId)) {
            log.debug("deleteItem: invalid itemId: {}", itemId);
            throw new RuntimeException("deleteItem: invalid itemId: " + itemId);
        }

        Optional<ItemEntity> entity = itemRepository.findById(UUID.fromString(itemId));

        if (entity.isPresent()) {
            itemRepository.delete(entity.get());
            log.debug("deleteItem: deleted item with id: {}", itemId);
        } else {
            log.debug("deleteItem: item with id: {} not found, nothing to delete.", itemId);
        }
    }

    public static boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private CategoryEntity getCategory(String categoryName) {
        return categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new RuntimeException("No category found of name: " + categoryName));
    }

    private StockTypeEntity getStockType(String stockTypeName) {
        return stockTypeRepository.findByName(stockTypeName)
                .orElseThrow(() -> new RuntimeException("No stock type found of name: " + stockTypeName));
    }

    private ItemTypeEntity getItemType(String itemTypeName) {
        return itemTypeRepository.findByName(itemTypeName)
                .orElseThrow(() -> new RuntimeException("No item type found of name: " + itemTypeName));
    }
}
