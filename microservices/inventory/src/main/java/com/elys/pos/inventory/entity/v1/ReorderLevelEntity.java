package com.elys.pos.inventory.entity.v1;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "reoder_levels")
public class ReorderLevelEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @NotNull(message = "Item cannot be null")
    @NotBlank(message = "Item cannot be empty")
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity item;

    @NotNull(message = "Packaging type cannot be null")
    @NotBlank(message = "Packaging type cannot be empty")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packaging_type_id", nullable = false)
    private PackagingTypeEntity packagingType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_of_measure_id")
    private UnitOfMeasureEntity unitOfMeasure;

    @Builder.Default
    @NotNull(message = "Reorder level threshold cannot be null")
    @DecimalMin(value = "0.00", message = "Reorder level threshold must be at least 0")
    @Digits(integer = 13, fraction = 2, message = "Reorder level threshold must have at most 15 total digits, with 2 decimal places")
    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal threshold = BigDecimal.ZERO;
}
