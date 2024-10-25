package com.elys.pos.inventory.persistence.v1;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

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
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "receivings_items_sequence"
    )
    @SequenceGenerator(
            name = "receivings_items_sequence",
            sequenceName = "receivings_items_sequence",
            allocationSize = 1
    )
    @Column(name = "receiving_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receiving_id", nullable = false)
    private ReceivingEntity receiving;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity item;

    private String description;

    private String serialNumber;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal receivingQuantity = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal quantityPurchased = BigDecimal.ZERO;

    private Long discountTypeId;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal discount = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "packaging_type_id", nullable = false)
    private PackagingTypeEntity packagingType;

    @Column(precision = 15, scale = 2)
    private BigDecimal packagingCapacity = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "unit_of_measure_id", nullable = false)
    private UnitOfMeasureEntity unitOfMeasure;
}
