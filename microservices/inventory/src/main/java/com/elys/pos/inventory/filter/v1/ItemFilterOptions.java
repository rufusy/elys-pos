package com.elys.pos.inventory.filter.v1;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
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

    @Override
    public String toString() {
        return "ItemFilterOptions{" +
                "name='" + name + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", itemNumber='" + itemNumber + '\'' +
                ", supplierId=" + supplierId +
                ", sellingPriceLowerBound=" + sellingPriceLowerBound +
                ", sellingPriceUpperBound=" + sellingPriceUpperBound +
                ", taxCategoryId=" + taxCategoryId +
                ", hsnCode='" + hsnCode + '\'' +
                ", itemTypeName='" + itemTypeName + '\'' +
                ", stockTypeName='" + stockTypeName + '\'' +
                ", serialized='" + serialized + '\'' +
                ", batchTracked='" + batchTracked + '\'' +
                ", " + super.toString() +
                '}';
    }
}
