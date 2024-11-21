package com.elys.pos.inventory.v1.service;

import com.elys.pos.inventory.v1.api.model.Category;
import com.elys.pos.inventory.v1.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import reactor.core.publisher.Mono;

public interface CategoryService {

    Page<Category> getCategories(Specification<CategoryEntity> specification, Pageable pageable);

    Mono<Category> getCategoryById(String categoryId);

    Mono<Category> createCategory(Category category);

    Mono<Category> updateCategory(Category category);

    Mono<Void> deleteCategory(String categoryId);
}
