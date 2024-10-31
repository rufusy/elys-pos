package com.elys.pos.inventory.repository.v1;

import com.elys.pos.inventory.entity.v1.CategoryEntity;
import com.elys.pos.inventory.entity.v1.ItemEntity;
import com.elys.pos.inventory.entity.v1.ItemTypeEntity;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class ItemSpecification {

    public static Specification<ItemEntity> hasCategoryNameContaining(String categoryName) {
        return (root, query, criteriaBuilder) -> {
            if (categoryName != null && !categoryName.isEmpty()) {
                Join<ItemEntity, CategoryEntity> category = root.join("category");
                return criteriaBuilder.like(criteriaBuilder.lower(category.get("name")), "%" + categoryName.toLowerCase() + "%");
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<ItemEntity> hasItemTypeNameContaining(String itemTypeName) {
        return (root, query, criteriaBuilder) -> {
            if (itemTypeName != null && !itemTypeName.isEmpty()) {
                Join<ItemEntity, ItemTypeEntity> itemType = root.join("itemType");
                return criteriaBuilder.like(criteriaBuilder.lower(itemType.get("name")), "%" + itemTypeName.toLowerCase() + "%");
            }
            return null;
        };
    }

    public static Specification<ItemEntity> hasStockTypeNameContaining(String stockTypeName) {
        return (root, query, criteriaBuilder) -> {
            if (stockTypeName != null && !stockTypeName.isEmpty()) {
                Join<ItemEntity, ItemTypeEntity> stockType = root.join("stockType");
                return criteriaBuilder.like(criteriaBuilder.lower(stockType.get("name")), "%" + stockTypeName.toLowerCase() + "%");
            }
            return null;
        };
    }

    public static Specification<ItemEntity> getItemsByCriteria(
            String itemName, String categoryName, String itemNumber, String supplierId, BigDecimal sellingPrice, BigDecimal sellingPrice2,
            String taxCategoryId, String hsnCode, String itemTypeName, String stockTypeName, boolean serialized, boolean batchTracked,
            LocalDate createdAt, LocalDate createdAt2) {

        return (root, query, criteriaBuilder) -> {

            Join<ItemEntity, CategoryEntity> category = root.join("category");
            Join<ItemEntity, ItemTypeEntity> itemType = root.join("itemType");
            Join<ItemEntity, ItemTypeEntity> stockType = root.join("stockType");

            if (query != null) {
                query.multiselect(root.get("id"), root.get("name"), root.get("createdAt"), root.get("description"),
                        root.get("itemNumber"), root.get("imageUrl"), root.get("supplierId"), root.get("sellingPrice"),
                        root.get("taxCategoryId"), root.get("hsnCode"), root.get("serialized"), root.get("batchTracked"),
                        category.get("name"), itemType.get("name"), stockType.get("name")
                );
            }

            Specification<ItemEntity> combinedSpec = Specification
                    .where(SpecificationUtils.<ItemEntity>stringFieldContains("name", itemName))
                    .and(hasCategoryNameContaining(categoryName))
                    .and(SpecificationUtils.stringFieldContains("itemNumber", itemNumber))
                    .and(SpecificationUtils.uuidFieldEquals("supplierId", supplierId))
                    .and(SpecificationUtils.numberFieldEqualTo("sellingPrice", sellingPrice))
                    .and(SpecificationUtils.numberFieldGreaterThan("sellingPrice", sellingPrice))
                    .and(SpecificationUtils.numberFieldLessThan("sellingPrice", sellingPrice))
                    .and(SpecificationUtils.numberFieldBetween("sellingPrice", sellingPrice, sellingPrice2))
                    .and(SpecificationUtils.uuidFieldEquals("taxCategoryId", taxCategoryId))
                    .and(SpecificationUtils.stringFieldContains("hsnCode", hsnCode))
                    .and(hasItemTypeNameContaining(itemTypeName))
                    .and(hasStockTypeNameContaining(stockTypeName))
                    .and(SpecificationUtils.dateFieldEquals("createdAt", createdAt))
                    .and(SpecificationUtils.dateFieldLessThan("createdAt", createdAt))
                    .and(SpecificationUtils.dateFieldGreaterThan("createdAt", createdAt))
                    .and(SpecificationUtils.dateFieldBetween("createdAt", createdAt, createdAt2))
                    .and(SpecificationUtils.booleanFieldEquals("serialized", serialized))
                    .and(SpecificationUtils.booleanFieldEquals("batchTracked", batchTracked));

            return combinedSpec.toPredicate(root, query, criteriaBuilder);
        };
    }
}
