package com.elys.pos.inventory.controller.v1;

import com.elys.pos.inventory.entity.v1.ItemEntity;
import com.elys.pos.inventory.filter.v1.ItemFilterOptions;
import com.elys.pos.inventory.repository.v1.ItemRepository;
import com.elys.pos.inventory.specification.v1.ItemSpecification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
public class ItemController {

    private final ObjectMapper objectMapper;
    private final ItemRepository itemRepository;
    private final ItemSpecification itemSpecification;
    private ItemFilterOptions filterOptions;

    public ItemController(ObjectMapper objectMapper, ItemRepository itemRepository, ItemSpecification itemSpecification) {
        this.objectMapper = objectMapper;
        this.itemRepository = itemRepository;
        this.itemSpecification = itemSpecification;
    }

    @GetMapping(
            value = "/items",
            produces = "application/json")
    public ResponseEntity<Page<ItemEntity>> index(
            @RequestParam(required = false, defaultValue = "") String param,
            @RequestParam(required = false, defaultValue = "") String sort,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {

        log.debug("Received param: {}", param);

        if (param != null && !param.isEmpty()) {
            try {
                filterOptions = objectMapper.readValue(param, ItemFilterOptions.class);
                log.debug("Parsed ItemFilterOptions: {}", filterOptions.toString());
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
            filterOptions = ItemFilterOptions.builder().build();
            log.debug("Using default ItemFilterOptions: {}", filterOptions.toString());
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
        Specification<ItemEntity> baseSpecification = itemSpecification.getItemsByCriteria(filterOptions);

        return ResponseEntity.status(OK).body(itemRepository.findAll(baseSpecification, pageable));
    }
}

