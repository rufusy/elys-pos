package com.elys.pos.inventory.projection.v1;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface ItemProjection {
    UUID getId();
    String getName();
    LocalDateTime getCreatedAt();
    String getDescription();
    String getItemNumber();
    String getImageUrl();
    UUID getSupplierId();
    BigDecimal getSellingPrice();
    UUID getTaxCategoryId();
    String getHsnCode();
    Boolean getSerialized();
    Boolean getBatchTracked();

    // Related entity projections
    String getCategoryName();
    String getItemTypeName();
    String getStockTypeName();
}

