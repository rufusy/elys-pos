package com.elys.pos.inventory.v1.controller;

import com.elys.pos.inventory.v1.api.model.Item;
import com.elys.pos.inventory.v1.api.resource.ItemResource;
import com.elys.pos.inventory.v1.entity.ItemEntity;
import com.elys.pos.inventory.v1.filter.ItemFilterOptions;
import com.elys.pos.inventory.v1.service.ItemService;
import com.elys.pos.inventory.v1.specification.ItemSpecification;
import com.elys.pos.util.v1.exception.InvalidInputException;
import com.elys.pos.util.v1.ValidatorUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
public class ItemController implements ItemResource {

    private final ObjectMapper objectMapper;
    private final ItemSpecification itemSpecification;
    private final ItemService itemService;
    private ItemFilterOptions itemFilterOptions;
    private final ValidatorUtil validatorUtil;

    public ItemController(ObjectMapper objectMapper, ItemSpecification itemSpecification, ItemService itemService, ValidatorUtil validatorUtil) {
        this.objectMapper = objectMapper;
        this.itemSpecification = itemSpecification;
        this.itemService = itemService;
        this.validatorUtil = validatorUtil;
    }

    public ResponseEntity<Page<Item>> getItems(String filter, String sort, int page, int size) {

        log.debug("Received filter options: {}", filter);

        if (filter != null && !filter.isEmpty()) {
            try {
                itemFilterOptions = objectMapper.readValue(filter, ItemFilterOptions.class);
                log.debug("Parsed ItemFilterOptions: {}", itemFilterOptions.toString());
            } catch (JsonProcessingException e) {
                log.warn("Error parsing JSON: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body("Error parsing parameters: " + e.getMessage());
            } catch (Exception e) {
                log.error("Unexpected error occurred: {}", e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Unexpected error: " + e.getMessage());
            }

        } else {
            itemFilterOptions = ItemFilterOptions.builder().build();
            log.debug("Using default ItemFilterOptions: {}", itemFilterOptions.toString());
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
        Specification<ItemEntity> spec = itemSpecification.getItemsByCriteria(itemFilterOptions);
        return ResponseEntity.status(OK).body(itemService.getItems(spec, pageable));
    }

    @Override
    public ResponseEntity<Mono<Item>> getItemById(String itemId) {
        validatorUtil.validateUUID(itemId);
        return ResponseEntity.status(OK).body(itemService.getItemById(itemId));
    }

    @Override
    public ResponseEntity<Mono<Item>> createItem(Item item) {
        return ResponseEntity.status(OK).body(itemService.createItem(item));
    }

    @Override
    public ResponseEntity<Mono<Item>> updateItem(Item item) {
        return ResponseEntity.status(OK).body(itemService.updateItem(item));
    }

    @Override
    public ResponseEntity<Mono<Void>> deleteItem(String itemId) {
        validatorUtil.validateUUID(itemId);
        return ResponseEntity.status(OK).body(itemService.deleteItem(itemId));
    }
}

