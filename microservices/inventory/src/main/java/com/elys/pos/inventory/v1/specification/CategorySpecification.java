package com.elys.pos.inventory.v1.specification;

import com.elys.pos.inventory.v1.entity.CategoryEntity;
import com.elys.pos.inventory.v1.filter.CategoryFilterOptions;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CategorySpecification {

    private Specification<CategoryEntity> parentNameEquals(String categoryName) {
        return ((root, query, criteriaBuilder) -> {
            if (categoryName != null && !categoryName.isEmpty()) {
                Join<CategoryEntity, CategoryEntity> category = root.join("parentCategory");
                return criteriaBuilder.equal(criteriaBuilder.lower(category.get("name")), categoryName.toLowerCase());
            }
            return criteriaBuilder.conjunction();
        });
    }

    public Specification<CategoryEntity> getCategoriesByCriteria(CategoryFilterOptions filterOptions) {
        return (root, query, criteriaBuilder) -> {
            Specification<CategoryEntity> combinedSpec = Specification
                    .where(SpecificationUtils.<CategoryEntity>stringFieldContains("name", filterOptions.getName()))
                    .and(SpecificationUtils.dateFieldBetween("createdAt",
                            filterOptions.getCreatedAtLowerBound(), filterOptions.getCreatedAtUpperBound()))
                    .and(SpecificationUtils.dateFieldBetween("updatedAt",
                            filterOptions.getUpdatedAtLowerBound(), filterOptions.getUpdatedAtUpperBound()))
                    .and(SpecificationUtils.dateFieldBetween("deletedAt",
                            filterOptions.getDeletedAtLowerBound(), filterOptions.getDeletedAtUpperBound()))
                    .and(SpecificationUtils.uuidFieldEquals("createdBy", filterOptions.getCreatedBy()))
                    .and(SpecificationUtils.uuidFieldEquals("updatedBy", filterOptions.getUpdatedBy()))
                    .and(SpecificationUtils.uuidFieldEquals("deletedBy", filterOptions.getDeletedBy()))
                    .and(parentNameEquals(filterOptions.getParentName()));

            if ("isDeleted".equals(filterOptions.getDeleted())) {
                combinedSpec = combinedSpec.and(SpecificationUtils.booleanFieldEquals("deleted", true));
            } else if ("isNotDeleted".equals(filterOptions.getDeleted())) {
                combinedSpec = combinedSpec.and(SpecificationUtils.booleanFieldEquals("deleted", false));
            }

            return combinedSpec.toPredicate(root, query, criteriaBuilder);
        };
    }
}
