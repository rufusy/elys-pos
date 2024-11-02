package com.elys.pos.inventory.specification.v1;

import com.elys.pos.inventory.entity.v1.CategoryEntity;
import com.elys.pos.inventory.entity.v1.ItemEntity;
import com.elys.pos.inventory.entity.v1.ItemTypeEntity;
import com.elys.pos.inventory.entity.v1.StockTypeEntity;
import com.elys.pos.inventory.filter.v1.ItemFilterOptions;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ItemSpecification {

    private Specification<ItemEntity> categoryNameEquals(String categoryName) {
        return (root, query, criteriaBuilder) -> {
            if (categoryName != null && !categoryName.isEmpty()) {
                Join<ItemEntity, CategoryEntity> category = root.join("category");
                return criteriaBuilder.equal(criteriaBuilder.lower(category.get("name")), categoryName.toLowerCase());
            }
            return criteriaBuilder.conjunction();
        };
    }

    private Specification<ItemEntity> itemTypeNameEquals(String itemTypeName) {
        return (root, query, criteriaBuilder) -> {
            if (itemTypeName != null && !itemTypeName.isEmpty()) {
                Join<ItemEntity, ItemTypeEntity> itemType = root.join("itemType");
                return criteriaBuilder.equal(criteriaBuilder.lower(itemType.get("name")), itemTypeName.toLowerCase());
            }
            return criteriaBuilder.conjunction();
        };
    }

    private Specification<ItemEntity> stockTypeNameEquals(String stockTypeName) {
        return (root, query, criteriaBuilder) -> {
            if (stockTypeName != null && !stockTypeName.isEmpty()) {
                Join<ItemEntity, StockTypeEntity> stockType = root.join("stockType");
                return criteriaBuilder.equal(criteriaBuilder.lower(stockType.get("name")), stockTypeName.toLowerCase());
            }
            return criteriaBuilder.conjunction();
        };
    }

    public Specification<ItemEntity> getItemsByCriteria(ItemFilterOptions filterOptions) {

        return (root, query, criteriaBuilder) -> {

            Join<ItemEntity, CategoryEntity> category = root.join("category");
            Join<ItemEntity, ItemTypeEntity> itemType = root.join("itemType");
            Join<ItemEntity, StockTypeEntity> stockType = root.join("stockType");

            if (query != null) {
                query.multiselect(root.get("id"), root.get("name"), root.get("createdAt"), root.get("description"),
                        root.get("itemNumber"), root.get("imageUrl"), root.get("supplierId"), root.get("sellingPrice"),
                        root.get("taxCategoryId"), root.get("hsnCode"), root.get("serialized"), root.get("batchTracked"),
                        category.get("name"), itemType.get("name"), stockType.get("name")
                );
            }

            Specification<ItemEntity> combinedSpec = Specification
                    .where(SpecificationUtils.<ItemEntity>stringFieldContains("name", filterOptions.getName()))
                    .and(SpecificationUtils.stringFieldContains("itemNumber", filterOptions.getItemNumber()))
                    .and(SpecificationUtils.uuidFieldEquals("supplierId", filterOptions.getSupplierId()))
                    .and(SpecificationUtils.numberFieldBetween("sellingPrice",
                            filterOptions.getSellingPriceLowerBound(), filterOptions.getSellingPriceUpperBound()))
                    .and(SpecificationUtils.uuidFieldEquals("taxCategoryId", filterOptions.getTaxCategoryId()))
                    .and(SpecificationUtils.stringFieldContains("hsnCode", filterOptions.getHsnCode()))
                    .and(SpecificationUtils.dateFieldBetween("createdAt",
                            filterOptions.getCreatedAtLowerBound(), filterOptions.getCreatedAtUpperBound()))
                    .and(SpecificationUtils.dateFieldBetween("updatedAt",
                            filterOptions.getUpdatedAtLowerBound(), filterOptions.getUpdatedAtUpperBound()))
                    .and(SpecificationUtils.dateFieldBetween("deletedAt",
                            filterOptions.getDeletedAtLowerBound(), filterOptions.getDeletedAtUpperBound()))
                    .and(categoryNameEquals(filterOptions.getCategoryName()))
                    .and(stockTypeNameEquals(filterOptions.getStockTypeName()))
                    .and(itemTypeNameEquals(filterOptions.getItemTypeName()));

            if ("isSerialized".equals(filterOptions.getSerialized())) {
                combinedSpec = combinedSpec.and(SpecificationUtils.booleanFieldEquals("serialized", true));
            } else if ("isNotSerialized".equals(filterOptions.getSerialized())) {
                combinedSpec = combinedSpec.and(SpecificationUtils.booleanFieldEquals("serialized", false));
            }

            if ("isBatchTracked".equals(filterOptions.getBatchTracked())) {
                combinedSpec = combinedSpec.and(SpecificationUtils.booleanFieldEquals("batchTracked", true));
            } else if ("isNotBatchTracked".equals(filterOptions.getBatchTracked())) {
                combinedSpec = combinedSpec.and(SpecificationUtils.booleanFieldEquals("batchTracked", false));
            }

            if ("isDeleted".equals(filterOptions.getDeleted())) {
                combinedSpec = combinedSpec.and(SpecificationUtils.booleanFieldEquals("deleted", true));
            } else if ("isNotDeleted".equals(filterOptions.getDeleted())) {
                combinedSpec = combinedSpec.and(SpecificationUtils.booleanFieldEquals("deleted", false));
            }

            return combinedSpec.toPredicate(root, query, criteriaBuilder);
        };
    }
}
