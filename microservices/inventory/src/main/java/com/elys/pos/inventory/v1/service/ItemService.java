package com.elys.pos.inventory.v1.service;

import com.elys.pos.inventory.v1.api.model.Item;
import com.elys.pos.inventory.v1.entity.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import reactor.core.publisher.Mono;

public interface ItemService {
    Page<Item> getItems(Specification<ItemEntity> specification, Pageable pageable);

    Mono<Item> getItemById(String itemId);

    Mono<Item> createItem(Item item);

    Mono<Item> updateItem(Item item);

    Mono<Void> deleteItem(String itemId);
}
