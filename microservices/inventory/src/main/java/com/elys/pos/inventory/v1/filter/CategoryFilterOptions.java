package com.elys.pos.inventory.v1.filter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
final public class CategoryFilterOptions extends FilterOptions {
    private String name;
    private String parentName;

    @Override
    public String toString() {
        return "ItemFilterOptions{" +
                "name='" + name + '\'' +
                ", parentCategoryName='" + parentName + '\'' +
                ", " + super.toString() +
                '}';
    }
}
