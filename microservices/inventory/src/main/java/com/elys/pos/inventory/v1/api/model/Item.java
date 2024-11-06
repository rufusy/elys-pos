package com.elys.pos.inventory.v1.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private UUID id;
    private String name;
    private String description;
    private String itemNumber;
    private String imageUrl;
    private UUID supplierId;
    private UUID taxCategoryId;
    private String hsnCode;
    private BigDecimal sellingPrice;
    private boolean serialized;
    private boolean batchTracked;
    private UUID createdBy;
    private LocalDateTime createdAt;
    private UUID updatedBy;
    private LocalDateTime updatedAt;
    private boolean deleted;
    private UUID deletedBy;
    private LocalDateTime deletedAt;
    private String serviceAddress;
    private Category category;
    private ItemType itemType;
    private StockType stockType;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category {
        private String name;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemType {
        private String name;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockType {
        private String name;
    }
}
