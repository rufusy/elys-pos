package com.elys.pos.inventory.filter.v1;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
abstract public class FilterOptions {
    private String createdBy;
    private LocalDate createdAtLowerBound;
    private LocalDate createdAtUpperBound;
    private String updatedBy;
    private LocalDate updatedAtLowerBound;
    private LocalDate updatedAtUpperBound;
    private String deleted;
    private String deletedBy;
    private LocalDate deletedAtLowerBound;
    private LocalDate deletedAtUpperBound;
}
