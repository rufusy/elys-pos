package com.elys.pos.inventory.repository.v1;

import com.elys.pos.inventory.entity.v1.CategoryEntity;
import com.elys.pos.inventory.entity.v1.ItemEntity;
import com.elys.pos.inventory.entity.v1.ItemTypeEntity;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Component
public class ItemSpecification {

    public static Specification<ItemEntity> hasItemNameContaining(String name) {
        return (root, query, criteriaBuilder) -> (name != null && !name.isEmpty())
                ? criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%")
                : null;
    }

    public static Specification<ItemEntity> hasCategoryNameContaining(String categoryName) {
        return (root, query, criteriaBuilder) -> {
            if (categoryName != null && !categoryName.isEmpty()) {
                Join<ItemEntity, CategoryEntity> category = root.join("category");
                return criteriaBuilder.like(criteriaBuilder.lower(category.get("name")), "%" + categoryName.toLowerCase() + "%");
            }
            return null;
        };
    }

    public static Specification<ItemEntity> hasItemNumberEqualTo(String itemNumber) {
        return (root, query, criteriaBuilder) -> (itemNumber != null && !itemNumber.isEmpty())
                ? criteriaBuilder.equal(root.get("itemNumber"), itemNumber)
                : null;
    }

    public static Specification<ItemEntity> hasSupplierIdEqualTo(String supplierId) {
        return ((root, query, criteriaBuilder) -> (supplierId != null && !supplierId.isEmpty())
                ? criteriaBuilder.equal(root.get("supplierId"), UUID.fromString(supplierId))
                : null
        );
    }

    public static Specification<ItemEntity> hasSellingPriceEqualTo(BigDecimal price) {
        return ((root, query, criteriaBuilder) -> (price != null)
                ? criteriaBuilder.equal(root.get("sellingPrice"), price)
                : null
        );
    }

    public static Specification<ItemEntity> hasSellingPriceGreaterThan(BigDecimal price) {
        return (root, query, criteriaBuilder) -> (price != null)
                ? criteriaBuilder.greaterThan(root.get("sellingPrice"), price)
                : null;
    }

    public static Specification<ItemEntity> hasSellingPriceLessThan(BigDecimal price) {
        return (root, query, criteriaBuilder) -> (price != null)
                ? criteriaBuilder.lessThan(root.get("sellingPrice"), price)
                : null;
    }

    public static Specification<ItemEntity> hasSellingPriceBetween(BigDecimal price1, BigDecimal price2) {
        return (root, query, criteriaBuilder) -> (price1 != null && price2 != null)
                ? criteriaBuilder.between(root.get("sellingPrice"), price1, price2)
                : null;
    }

    public static Specification<ItemEntity> hasTaxCategoryIdEqualTo(String taxCategoryId) {
        return ((root, query, criteriaBuilder) -> (taxCategoryId != null && !taxCategoryId.isEmpty())
                ? criteriaBuilder.equal(root.get("taxCategoryId"), UUID.fromString(taxCategoryId))
                : null
        );
    }

    public static Specification<ItemEntity> hasHsnCodeEqualTo(String hsnCode) {
        return ((root, query, criteriaBuilder) -> (hsnCode != null && !hsnCode.isEmpty())
                ? criteriaBuilder.equal(root.get("hsnCode"), UUID.fromString(hsnCode))
                : null
        );
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

    public static Specification<ItemEntity> hasCreatedAtEqualTo(LocalDate date) {
        return (root, query, criteriaBuilder) -> date != null
                ? criteriaBuilder.equal(root.get("createdAt"), date)
                : null;
    }

    public static Specification<ItemEntity> hasCreatedAtLessThan(LocalDate date) {
        return (root, query, criteriaBuilder) -> date != null
                ? criteriaBuilder.lessThan(root.get("createdAt"), date)
                : null;
    }

    public static Specification<ItemEntity> hasCreatedAtGreaterThan(LocalDate date) {
        return (root, query, criteriaBuilder) -> date != null
                ? criteriaBuilder.greaterThan(root.get("createdAt"), date)
                : null;
    }

    public static Specification<ItemEntity> hasCreatedAtBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("createdAt"), startDate, endDate);
            }
            return null;
        };
    }

    public Specification<ItemEntity> hasSerializedTrue() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("serialized"), true);
    }

    public Specification<ItemEntity> hasSerializedFalse() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("serialized"), false);
    }

    public Specification<ItemEntity> hasBatchTrackedTrue() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("batchTracked"), true);
    }

    public Specification<ItemEntity> hasBatchTrackedFalse() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("batchTracked"), false);
    }

    public Specification<ItemEntity> getItemsByCriteria(
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
                    .where(hasItemNameContaining(itemName))
                    .and(hasCategoryNameContaining(categoryName))
                    .and(hasItemNumberEqualTo(itemNumber))
                    .and(hasSupplierIdEqualTo(supplierId))
                    .and(hasSellingPriceEqualTo(sellingPrice))
                    .and(hasSellingPriceGreaterThan(sellingPrice))
                    .and(hasSellingPriceLessThan(sellingPrice))
                    .and(hasSellingPriceBetween(sellingPrice, sellingPrice2))
                    .and(hasTaxCategoryIdEqualTo(taxCategoryId))
                    .and(hasHsnCodeEqualTo(hsnCode))
                    .and(hasItemTypeNameContaining(itemTypeName))
                    .and(hasStockTypeNameContaining(stockTypeName))
                    .and(hasCreatedAtEqualTo(createdAt))
                    .and(hasCreatedAtLessThan(createdAt))
                    .and(hasCreatedAtGreaterThan(createdAt))
                    .and(hasCreatedAtBetween(createdAt, createdAt2));

            if (Boolean.TRUE.equals(serialized)) {
                combinedSpec = combinedSpec.and(hasSerializedTrue());
            } else if (Boolean.FALSE.equals(serialized)) {
                combinedSpec = combinedSpec.and(hasSerializedFalse());
            }

            if (Boolean.TRUE.equals(batchTracked)) {
                combinedSpec = combinedSpec.and((hasBatchTrackedTrue()));
            } else if (Boolean.FALSE.equals(batchTracked)) {
                combinedSpec = combinedSpec.and(hasBatchTrackedFalse());
            }

            return combinedSpec.toPredicate(root, query, criteriaBuilder);
        };
    }
}
