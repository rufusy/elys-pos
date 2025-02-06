package com.elys.pos.inventory.v1.controller;

import com.elys.pos.inventory.v1.api.model.item.CreateItem;
import com.elys.pos.inventory.v1.api.model.item.Item;
import com.elys.pos.inventory.v1.api.model.item.UpdateItem;
import com.elys.pos.inventory.v1.api.resource.ItemResource;
import com.elys.pos.inventory.v1.entity.ItemEntity;
import com.elys.pos.inventory.v1.filter.ItemFilterOptions;
import com.elys.pos.inventory.v1.service.ItemService;
import com.elys.pos.inventory.v1.specification.ItemSpecification;
import com.elys.pos.inventory.v1.specification.SpecificationUtils;
import com.elys.pos.util.v1.exception.InvalidInputException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Validated
@RestController
public class ItemController implements ItemResource {

    private final ObjectMapper objectMapper;
    private final ItemSpecification itemSpecification;
    private final ItemService itemService;

    public ItemController(ObjectMapper objectMapper, ItemSpecification itemSpecification, ItemService itemService) {
        this.objectMapper = objectMapper;
        this.itemSpecification = itemSpecification;
        this.itemService = itemService;
    }

    public ResponseEntity<Page<Item>> getItems(String filter, String sort, int page, int size) {

        ItemFilterOptions filterOptions;

        if (filter != null && !filter.isEmpty()) {
            try {
                filterOptions = objectMapper.readValue(filter, ItemFilterOptions.class);
                log.debug("getItems: using parsed filter options: {}", filterOptions.toString());
            } catch (Exception e) {
                log.debug("getItems: error parsing filter options: {}", e.getMessage());
                throw new InvalidInputException("Error parsing filter options");
            }
        } else {
            filterOptions = ItemFilterOptions.builder().build();
            log.debug("getItems: using default filter options: {}", filterOptions.toString());
        }

        Sort sorting = Sort.unsorted();
        if (!sort.isEmpty()) { // Example sort string "sellingPrice:desc,name:asc"
            String[] sortParams = sort.split(",");
            for (String sortParam : sortParams) {
                String[] sortParts = sortParam.split(":");
                if (sortParts.length == 2) {
                    String field = sortParts[0];
                    Sort.Direction direction = Sort.Direction.fromString(sortParts[1]);
                    sorting = sorting.and(Sort.by(direction, field));
                } else if (sortParts.length == 1) {
                    sorting = sorting.and(Sort.by(Sort.Direction.ASC, sortParts[0]));
                }
            }
        }

        Pageable pageable = PageRequest.of(page, size, sorting);
        Specification<ItemEntity> spec = itemSpecification.getItemsByCriteria(filterOptions)
                .and(SpecificationUtils.booleanFieldEquals("deleted", false));
        return ResponseEntity.status(OK).body(itemService.getItems(spec, pageable));
    }

    @Override
    public ResponseEntity<Mono<Item>> getItemById(String itemId) {
        return ResponseEntity.status(OK).body(itemService.getItemById(itemId));
    }

    @Override
    public ResponseEntity<Mono<Item>> createItem(CreateItem item) {
        return ResponseEntity.status(OK).body(itemService.createItem(item));
    }

    @Override
    public ResponseEntity<Mono<Item>> updateItem(UpdateItem item) {
        return ResponseEntity.status(OK).body(itemService.updateItem(item));
    }

    @Override
    public ResponseEntity<Mono<Void>> deleteItem(String itemId) {
        return ResponseEntity.status(OK).body(itemService.deleteItem(itemId));
    }
}

