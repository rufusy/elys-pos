package com.elys.pos.inventory.entity.v1;

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
@Table(name = "receivings_items", indexes = {
        @Index(name = "receivings_items_unique", unique = true, columnList = "receiving_id,item_id")
})
public class ReceivingItemEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiving_id", nullable = false)
    private ReceivingEntity receiving;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity item;

    private String description;

    private String serialNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    private BatchEntity batch;

    @Builder.Default
    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal receivingQuantity = BigDecimal.ZERO;

    @Builder.Default
    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal quantityPurchased = BigDecimal.ZERO;

    private Long discountTypeId;

    @Builder.Default
    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal discount = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packaging_type_id", nullable = false)
    private PackagingTypeEntity packagingType;

    @Builder.Default
    @Column(precision = 15, scale = 2)
    private BigDecimal packagingCapacity = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_of_measure_id", nullable = false)
    private UnitOfMeasureEntity unitOfMeasure;
}
