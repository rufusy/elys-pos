package com.elys.pos.inventory.v1.service;

import com.elys.pos.inventory.v1.api.model.category.Category;
import com.elys.pos.inventory.v1.api.model.category.CreateCategory;
import com.elys.pos.inventory.v1.api.model.category.UpdateCategory;
import com.elys.pos.inventory.v1.entity.CategoryEntity;
import com.elys.pos.inventory.v1.mapper.category.CategoryMapper;
import com.elys.pos.inventory.v1.mapper.category.CreateCategoryMapper;
import com.elys.pos.inventory.v1.mapper.category.UpdateCategoryMapper;
import com.elys.pos.inventory.v1.repository.CategoryRepository;
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
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CreateCategoryMapper createCategoryMapper;
    private final UpdateCategoryMapper updateCategoryMapper;
    @Qualifier("jdbcScheduler")
    private final Scheduler jdbcScheduler;
    private final ServiceUtil serviceUtil;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper, CreateCategoryMapper createCategoryMapper, UpdateCategoryMapper updateCategoryMapper, Scheduler jdbcScheduler,
                               ServiceUtil serviceUtil) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.createCategoryMapper = createCategoryMapper;
        this.updateCategoryMapper = updateCategoryMapper;
        this.jdbcScheduler = jdbcScheduler;
        this.serviceUtil = serviceUtil;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Category> getCategories(Specification<CategoryEntity> specification, Pageable pageable) {
        log.debug("getCategories: will try to get all categories not flagged as deleted matching given specification");
        return categoryRepository.findAll(specification, pageable).map(categoryMapper::entityToApi).map(this::setServiceAddress);
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<Category> getCategoryById(String categoryId) {
        return getUsernameFromToken()
                .flatMap(username -> Mono.fromCallable(() -> internalGetCategoryById(categoryId, username))
                        .subscribeOn(jdbcScheduler));
    }

    private Category internalGetCategoryById(String categoryId, String username ) {
        log.debug("getCategory: user: {}, will try to get a category of id: {}", username, categoryId);
        CategoryEntity entity = categoryRepository.findById(UUID.fromString(categoryId))
                .orElseThrow(() -> new NotFoundException("No category found for categoryId: " + categoryId));
        return setServiceAddress(categoryMapper.entityToApi(entity));
    }

    @Transactional
    @Override
    public Mono<Category> createCategory(CreateCategory body) {
        return getUsernameFromToken()
                .flatMap(username -> Mono.fromCallable(() -> internalCreateCategory(body, username))
                        .subscribeOn(jdbcScheduler));
    }

    private Category internalCreateCategory(CreateCategory body, String username) {
        log.debug("createCategory: user: {}, will try to create a category of name: {}", username, body.getName());
        CategoryEntity entity = createCategoryMapper.apiToEntity(body);
        entity.setCreatedBy(username);
        entity.setCreatedAt(LocalDateTime.now());
        if (body.getParentCategory() != null) {
            entity.setParentCategory(getCategory(body.getParentCategory().getName()));
        }
        CategoryEntity savedEntity = categoryRepository.save(entity);
        log.debug("createCategory: created a category of name: {}", body.getName());
        return setServiceAddress(categoryMapper.entityToApi(savedEntity));
    }

    @Transactional
    @Override
    public Mono<Category> updateCategory(UpdateCategory body) {
        return getUsernameFromToken()
                .flatMap(username -> Mono.fromCallable(() -> internalUpdateCategory(body, username))
                        .subscribeOn(jdbcScheduler));
    }

    private Category internalUpdateCategory(UpdateCategory body, String username) {
        log.debug("updateCategory: user: {}, will try to update a category of name: {}", username, body.getName());
        CategoryEntity entity = updateCategoryMapper.apiToEntity(body);
        CategoryEntity existingCat = categoryRepository.findById(entity.getId())
                .orElseThrow(() -> new NotFoundException("No Category found with id: " + body.getId()));
        existingCat.setName(entity.getName());
        existingCat.setDescription(entity.getDescription());
        existingCat.setUpdatedBy(username);
        existingCat.setUpdatedAt(LocalDateTime.now());
        if (body.getParentCategory() != null) {
            existingCat.setParentCategory(getCategory(body.getParentCategory().getName()));
        } else {
            existingCat.setParentCategory(null);
        }
        CategoryEntity updated = categoryRepository.save(existingCat);
        log.debug("updateCategory: updated a category of id: {}", body.getId());
        return setServiceAddress(categoryMapper.entityToApi(updated));
    }

    @Transactional
    @Override
    public Mono<Void> deleteCategory(String categoryId) {
        return getUsernameFromToken()
                .flatMap(username -> Mono.fromRunnable(() -> internalDeleteCategory(categoryId, username))
                        .subscribeOn(jdbcScheduler).then());
    }

    private void internalDeleteCategory(String categoryId, String username) {
        log.debug("deleteCategory: user: {}, will try to delete a category with id: {}", username, categoryId);
        CategoryEntity entity = categoryRepository.findById(UUID.fromString(categoryId))
                .orElseThrow(() -> new NotFoundException("No Category found with id: " + categoryId));
        categoryRepository.flagAsDeleted(UUID.fromString(categoryId), username, LocalDateTime.now());
        log.debug("deleteCategory: deleted category with id: {}", categoryId);
    }

    private CategoryEntity getCategory(String categoryName) {
        return categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new NotFoundException("No category found of name: " + categoryName));
    }

    private Category setServiceAddress(Category e) {
        e.setServiceAddress(serviceUtil.getServiceAddress());
        return e;
    }

    private Mono<String> getUsernameFromToken() {
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> (Jwt) ctx.getAuthentication().getPrincipal())
                .map(jwt -> jwt.getClaimAsString("preferred_username"));
    }
}
