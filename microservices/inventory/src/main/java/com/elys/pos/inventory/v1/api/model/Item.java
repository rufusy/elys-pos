package com.elys.pos.inventory.v1.api.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
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
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category {
        private UUID id;
        private String name;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemType {
        private UUID id;
        private String name;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockType {
        private UUID id;
        private String name;
    }
}
