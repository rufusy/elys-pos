package com.elys.pos.inventory.v1.service;

import com.elys.pos.inventory.v1.api.model.Item;
import com.elys.pos.inventory.v1.entity.ItemEntity;
import com.elys.pos.inventory.v1.mapper.ItemMapper;
import com.elys.pos.inventory.v1.repository.ItemRepository;
import com.elys.pos.inventory.v1.specification.SpecificationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemServiceImpl(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Item> getItems(Specification<ItemEntity> specification, Pageable pageable) {
        Specification<ItemEntity> combinedSpec = specification
                .and(SpecificationUtils.booleanFieldEquals("deleted", false));

        return itemRepository.findAll(combinedSpec, pageable).map(itemMapper::entityToApi);
    }
}
