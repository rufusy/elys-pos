package com.elys.pos.inventory.filter.v1;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
public class ItemFilterOptions {
    private String name;
    private String categoryName;
    private String itemNumber;
    private UUID supplierId;
    private BigDecimal sellingPriceLowerBound;
    private BigDecimal sellingPriceUpperBound;
    private UUID taxCategoryId;
    private String hsnCode;
    private String itemTypeName;
    private String stockTypeName;
    private String serialized;
    private String batchTracked;
    private LocalDate createdAtLowerBound;
    private LocalDate createdAtUpperBound;
}
