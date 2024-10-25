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
@Table(name = "kits_items", indexes = {
        @Index(name = "kits_items_batches_unique", unique = true, columnList = "kit_id,item_id,batch_id")
})
public class KitItemEntity extends BaseEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "kits_items_sequence"
    )
    @SequenceGenerator(
            name = "kits_items_sequence",
            sequenceName = "kits_items_sequence",
            allocationSize = 1
    )
    @Column(name = "kit_item_id")
    private Long id;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal quantity = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity item;

    @ManyToOne
    @JoinColumn(name = "kit_id", nullable = false)
    private KitEntity kit;

    @ManyToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private BatchEntity batch;
}
