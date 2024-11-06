package com.elys.pos.inventory.v1.service;

import com.elys.pos.inventory.v1.api.model.Item;
import com.elys.pos.inventory.v1.entity.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface ItemService {
    Page<Item> getItems(Specification<ItemEntity> specification, Pageable pageable);
}
