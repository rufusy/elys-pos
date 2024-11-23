package com.elys.pos.inventory.v1.service;

import com.elys.pos.inventory.v1.api.model.Category;
import com.elys.pos.inventory.v1.entity.CategoryEntity;
import com.elys.pos.inventory.v1.mapper.CategoryMapper;
import com.elys.pos.inventory.v1.repository.CategoryRepository;
import com.elys.pos.util.v1.ServiceUtil;
import com.elys.pos.util.v1.exception.InvalidInputException;
import com.elys.pos.util.v1.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    @Qualifier("jdbcScheduler")
    private final Scheduler jdbcScheduler;
    private final ServiceUtil serviceUtil;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper, Scheduler jdbcScheduler,
                               ServiceUtil serviceUtil) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
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
        return Mono.fromCallable(() -> internalGetCategoryById(categoryId)).subscribeOn(jdbcScheduler);
    }

    private Category internalGetCategoryById(String categoryId) {
        log.debug("getCategory: will try to get a category of id: {}", categoryId);
        CategoryEntity entity = categoryRepository.findById(UUID.fromString(categoryId))
                .orElseThrow(() -> new NotFoundException("No category found for categoryId: " + categoryId));
        return setServiceAddress(categoryMapper.entityToApi(entity));
    }

    @Transactional
    @Override
    public Mono<Category> createCategory(Category body) {
        return Mono.fromCallable(() -> internalCreateCategory(body)).subscribeOn(jdbcScheduler);
    }

    private Category internalCreateCategory(Category body) {
        log.debug("createCategory: will try to create a category of name: {}", body.getName());
        CategoryEntity entity = categoryMapper.apiToEntity(body);
        if (entity.getCreatedBy() == null) {
            log.debug("createCategory: missing created by field");
            throw new InvalidInputException("Missing created by field");
        }
        entity.setCreatedAt(LocalDateTime.now());
        if (body.getParentCategory() != null) {
            entity.setParentCategory(getCategory(body.getParentCategory().getName()));
        }
        CategoryEntity saved = categoryRepository.save(entity);
        log.debug("createCategory: created a category of name: {}", body.getName());
        return setServiceAddress(categoryMapper.entityToApi(saved));
    }

    @Transactional
    @Override
    public Mono<Category> updateCategory(Category body) {
        return Mono.fromCallable(() -> internalUpdateCategory(body)).subscribeOn(jdbcScheduler);
    }

    private Category internalUpdateCategory(Category body) {
        log.debug("updateCategory: will try to update a category of name: {}", body.getName());
        CategoryEntity entity = categoryMapper.apiToEntity(body);
        if (entity.getUpdatedBy() == null) {
            log.debug("updateCategory: missing updated by field");
            throw new InvalidInputException("Missing updated by field");
        }
        CategoryEntity existingCat = categoryRepository.findById(entity.getId())
                .orElseThrow(() -> new NotFoundException("No Category found with id: " + body.getId()));
        existingCat.setName(entity.getName());
        existingCat.setDescription(entity.getDescription());
        existingCat.setUpdatedBy(entity.getUpdatedBy());
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

    private CategoryEntity getCategory(String categoryName) {
        return categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new NotFoundException("No category found of name: " + categoryName));
    }

    @Transactional
    @Override
    public Mono<Void> deleteCategory(String categoryId) {
        return Mono.fromRunnable(() -> internalDeleteCategory(categoryId)).subscribeOn(jdbcScheduler).then();
    }

    private void internalDeleteCategory(String categoryId) {
        log.debug("deleteCategory: will try to delete a category with id: {}", categoryId);
        CategoryEntity entity = categoryRepository.findById(UUID.fromString(categoryId))
                .orElseThrow(() -> new NotFoundException("No Category found with id: " + categoryId));
        categoryRepository.flagAsDeleted(UUID.fromString(categoryId), entity.getCreatedBy(), LocalDateTime.now());
        log.debug("deleteCategory: deleted category with id: {}", categoryId);
    }

    private Category setServiceAddress(Category e) {
        e.setServiceAddress(serviceUtil.getServiceAddress());
        return e;
    }
}
