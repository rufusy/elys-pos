package com.elys.pos.inventory.v1.service;

import com.elys.pos.inventory.v1.api.model.item.CreateItem;
import com.elys.pos.inventory.v1.api.model.item.Item;
import com.elys.pos.inventory.v1.api.model.item.UpdateItem;
import com.elys.pos.inventory.v1.entity.CategoryEntity;
import com.elys.pos.inventory.v1.entity.ItemEntity;
import com.elys.pos.inventory.v1.entity.ItemTypeEntity;
import com.elys.pos.inventory.v1.entity.StockTypeEntity;
import com.elys.pos.inventory.v1.mapper.item.CreateItemMapper;
import com.elys.pos.inventory.v1.mapper.item.ItemMapper;
import com.elys.pos.inventory.v1.mapper.item.UpdateItemMapper;
import com.elys.pos.inventory.v1.repository.CategoryRepository;
import com.elys.pos.inventory.v1.repository.ItemRepository;
import com.elys.pos.inventory.v1.repository.ItemTypeRepository;
import com.elys.pos.inventory.v1.repository.StockTypeRepository;
import com.elys.pos.util.v1.ServiceUtil;
import com.elys.pos.util.v1.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final StockTypeRepository stockTypeRepository;
    private final ItemTypeRepository itemTypeRepository;
    private final ItemMapper itemMapper;
    private final CreateItemMapper createItemMapper;
    private final UpdateItemMapper updateItemMapper;
    @Qualifier("jdbcScheduler")
    private final Scheduler jdbcScheduler;
    private final ServiceUtil serviceUtil;

    public ItemServiceImpl(ItemRepository itemRepository, CategoryRepository categoryRepository,
                           StockTypeRepository stockTypeRepository, ItemTypeRepository itemTypeRepository,
                           ItemMapper itemMapper, Scheduler jdbcScheduler,
                           ServiceUtil serviceUtil, CreateItemMapper createItemMapper, UpdateItemMapper updateItemMapper) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.stockTypeRepository = stockTypeRepository;
        this.itemTypeRepository = itemTypeRepository;
        this.itemMapper = itemMapper;
        this.jdbcScheduler = jdbcScheduler;
        this.serviceUtil = serviceUtil;
        this.createItemMapper = createItemMapper;
        this.updateItemMapper = updateItemMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Item> getItems(Specification<ItemEntity> specification, Pageable pageable) {
        log.debug("getItems: will try to get all items not flagged as deleted matching given specification");
        return itemRepository.findAll(specification, pageable).map(itemMapper::entityToApi).map(this::setServiceAddress);
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<Item> getItemById(String itemId) {
        return getUsernameFromToken()
                .flatMap(username -> Mono.fromCallable(() -> internalGetItemById(itemId, username))
                        .subscribeOn(jdbcScheduler));
    }

    private Item internalGetItemById(String itemId, String username) {
        log.debug("getItem: user: {}, will try to get an item of id: {}", username, itemId);
        ItemEntity entity = itemRepository.findById(UUID.fromString(itemId))
                .orElseThrow(() -> new NotFoundException("No item found for itemId: " + itemId));
        return setServiceAddress(itemMapper.entityToApi(entity));
    }

    @Transactional
    @Override
    public Mono<Item> createItem(CreateItem body) {
        return getUsernameFromToken()
                .flatMap(username -> Mono.fromCallable(() -> internalCreateItem(body, username))
                        .subscribeOn(jdbcScheduler));
    }

    private Item internalCreateItem(CreateItem body, String username) {
        log.debug("createItem: user: {}, will try to create an item of name: {}", username, body.getName());
        ItemEntity itemEntity = createItemMapper.apiToEntity(body);
        itemEntity.setCreatedBy(username);
        itemEntity.setCreatedAt(LocalDateTime.now());
        itemEntity.setCategory(this.getCategory(body.getCategory().getName()));
        itemEntity.setItemType(this.getItemType(body.getItemType().getName()));
        itemEntity.setStockType(this.getStockType(body.getStockType().getName()));
        ItemEntity newItemEntity = itemRepository.save(itemEntity);
        log.debug("createItem: created an item of name: {}", body.getName());
        return setServiceAddress(itemMapper.entityToApi(newItemEntity));
    }

    @Transactional
    @Override
    public Mono<Item> updateItem(UpdateItem body) {
        return getUsernameFromToken()
                .flatMap(username -> Mono.fromCallable(() -> internalUpdateItem(body, username))
                        .subscribeOn(jdbcScheduler));
    }

    private Item internalUpdateItem(UpdateItem body, String username) {
        log.debug("updateItem: user: {}, will try to update an item of name: {}", username, body.getName());
        ItemEntity itemEntity = updateItemMapper.apiToEntity(body);
        ItemEntity existingItemEntity = itemRepository.findById(itemEntity.getId())
                .orElseThrow(() -> new NotFoundException("No Item found with id: " + body.getId()));
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
        existingItemEntity.setUpdatedBy(username);
        existingItemEntity.setUpdatedAt(LocalDateTime.now());
        existingItemEntity.setCategory(this.getCategory(body.getCategory().getName()));
        existingItemEntity.setItemType(this.getItemType(body.getItemType().getName()));
        existingItemEntity.setStockType(this.getStockType(body.getStockType().getName()));
        ItemEntity updatedItem = itemRepository.save(existingItemEntity);
        log.debug("updateItem: updated an item of id: {}", updatedItem.getId());
        return setServiceAddress(itemMapper.entityToApi(updatedItem));
    }

    @Transactional
    @Override
    public Mono<Void> deleteItem(String itemId) {
        return getUsernameFromToken()
                .flatMap(username -> Mono.fromRunnable(() -> internalDeleteItem(itemId, username))
                        .subscribeOn(jdbcScheduler).then());
    }

    private void internalDeleteItem(String itemId, String username) {
        log.debug("deleteItem: user: {}, will try to delete an item with id: {}", username, itemId);
        itemRepository.findById(UUID.fromString(itemId))
                .orElseThrow(() -> new NotFoundException("No Item found with id: " + itemId));
        itemRepository.flagAsDeleted(UUID.fromString(itemId), username, LocalDateTime.now());
        log.debug("deleteItem: deleted item with id: {}", itemId);
    }

    private CategoryEntity getCategory(String categoryName) {
        return categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new NotFoundException("No category found of name: " + categoryName));
    }

    private StockTypeEntity getStockType(String stockTypeName) {
        return stockTypeRepository.findByName(stockTypeName)
                .orElseThrow(() -> new NotFoundException("No stock type found of name: " + stockTypeName));
    }

    private ItemTypeEntity getItemType(String itemTypeName) {
        return itemTypeRepository.findByName(itemTypeName)
                .orElseThrow(() -> new NotFoundException("No item type found of name: " + itemTypeName));
    }

    private Item setServiceAddress(Item e) {
        e.setServiceAddress(serviceUtil.getServiceAddress());
        return e;
    }

    private Mono<String> getUsernameFromToken() {
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> (Jwt) ctx.getAuthentication().getPrincipal())
                .map(jwt -> jwt.getClaimAsString("preferred_username"));
    }
}
