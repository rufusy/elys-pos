package com.elys.pos.inventory.v1.api.model.item;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private String id;
    private String name;
    private String description;
    private String itemNumber;
    private String imageUrl;
    private String supplierId;
    private String taxCategoryId;
    private String hsnCode;
    private BigDecimal sellingPrice;
    private boolean serialized;
    private boolean batchTracked;
    private String createdBy;
    private String createdAt;
    private String updatedBy;
    private String updatedAt;
    private boolean deleted;
    private String deletedBy;
    private String deletedAt;
    private String serviceAddress;
    private Category category;
    private ItemType itemType;
    private StockType stockType;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category {
        private String name;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemType {
        private String name;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockType {
        private String name;
    }
}
