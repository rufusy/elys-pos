package com.elys.pos.inventory.v1.controller;

import com.elys.pos.inventory.v1.api.model.Category;
import com.elys.pos.inventory.v1.api.resource.CategoryResource;
import com.elys.pos.inventory.v1.entity.CategoryEntity;
import com.elys.pos.inventory.v1.filter.CategoryFilterOptions;
import com.elys.pos.inventory.v1.service.CategoryService;
import com.elys.pos.inventory.v1.specification.CategorySpecification;
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

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Validated
@RestController
public class CategoryController implements CategoryResource {

    private final ObjectMapper objectMapper;
    private final CategoryService categoryService;
    private final CategorySpecification specification;

    public CategoryController(ObjectMapper objectMapper, CategoryService categoryService, CategorySpecification specification) {
        this.objectMapper = objectMapper;
        this.categoryService = categoryService;
        this.specification = specification;
    }

    @Override
    public ResponseEntity<Page<Category>> getCategories(String filter, String sort, int page, int size) {

        CategoryFilterOptions filterOptions;

        if (filter != null && !filter.isEmpty()) {
            try {
                filterOptions = objectMapper.readValue(filter, CategoryFilterOptions.class);
                log.debug("getCategories: using parsed filter options: {}", filterOptions.toString());
            } catch (Exception e) {
                log.debug("getCategories: error parsing filter options: {}", e.getMessage());
                throw new InvalidInputException("Error parsing filter options");
            }
        } else {
            filterOptions = CategoryFilterOptions.builder().build();
            log.debug("getCategories: using default filter options: {}", filterOptions.toString());
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
        Specification<CategoryEntity> spec = specification.getCategoriesByCriteria(filterOptions)
                .and(SpecificationUtils.booleanFieldEquals("deleted", false));
        return ResponseEntity.status(OK).body(categoryService.getCategories(spec, pageable));
    }

    @Override
    public ResponseEntity<Mono<Category>> getCategoryById(String categoryId) {
        return ResponseEntity.status(OK).body(categoryService.getCategoryById(categoryId));
    }

    @Override
    public ResponseEntity<Mono<Category>> createCategory(Category body) {
        return ResponseEntity.status(CREATED).body(categoryService.createCategory(body));
    }

    @Override
    public ResponseEntity<Mono<Category>> updateCategory(Category body) {
        return ResponseEntity.status(OK).body(categoryService.updateCategory(body));
    }

    @Override
    public ResponseEntity<Mono<Void>> deleteItem(String categoryId) {
        return ResponseEntity.status(OK).body(categoryService.deleteCategory(categoryId));
    }
}
