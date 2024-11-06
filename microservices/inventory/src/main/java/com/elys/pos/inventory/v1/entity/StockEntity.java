package com.elys.pos.inventory.v1.entity;

import jakarta.persistence.*;
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
@Table(name = "stock", indexes = {
        @Index(name = "stock_locations_batches_unique", unique = true, columnList = "location_id,batch_id")
})
public class StockEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Builder.Default
    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal quantity = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private LocationEntity location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    private BatchEntity batch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packaging_type_id", nullable = false)
    private PackagingTypeEntity packagingType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_of_measure_id", nullable = false)
    private UnitOfMeasureEntity unitOfMeasure;
}
