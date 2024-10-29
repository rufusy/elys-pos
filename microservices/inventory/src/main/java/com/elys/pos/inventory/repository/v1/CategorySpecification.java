package com.elys.pos.inventory.repository.v1;

import com.elys.pos.inventory.entity.v1.CategoryEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class CategorySpecification {

    public Specification<CategoryEntity> getCategoriesByCriteria(String name, LocalDate date, UUID parentCategoryId) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (name != null && !name.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if(date != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("createdAt"), date));
            }

            if (parentCategoryId != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("parentCategory").get("id"), parentCategoryId));
            }

            return predicate;
        };
    }
}
