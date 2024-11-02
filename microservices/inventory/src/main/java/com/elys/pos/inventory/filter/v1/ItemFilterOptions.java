package com.elys.pos.inventory.filter.v1;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
final public class ItemFilterOptions extends FilterOptions {
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
}
