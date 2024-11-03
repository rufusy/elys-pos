package com.elys.pos.inventory.filter.v1;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
abstract public class FilterOptions {
    private UUID createdBy;
    private LocalDate createdAtLowerBound;
    private LocalDate createdAtUpperBound;
    private UUID updatedBy;
    private LocalDate updatedAtLowerBound;
    private LocalDate updatedAtUpperBound;
    private String deleted;
    private UUID deletedBy;
    private LocalDate deletedAtLowerBound;
    private LocalDate deletedAtUpperBound;
}
