package com.elys.pos.inventory.v1.filter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
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

    @Override
    public String toString() {
        return "createdBy=" + createdBy +
                ", createdAtLowerBound=" + createdAtLowerBound +
                ", createdAtUpperBound=" + createdAtUpperBound +
                ", updatedBy=" + updatedBy +
                ", updatedAtLowerBound=" + updatedAtLowerBound +
                ", updatedAtUpperBound=" + updatedAtUpperBound +
                ", deleted='" + deleted + '\'' +
                ", deletedBy=" + deletedBy +
                ", deletedAtLowerBound=" + deletedAtLowerBound +
                ", deletedAtUpperBound=" + deletedAtUpperBound;
    }
}