package com.elys.pos.inventory.persistence.v1;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "batches")
public class BatchEntity extends BaseEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "batches_sequence"
    )
    @SequenceGenerator(
            name = "batches_sequence",
            sequenceName = "batches_sequence",
            allocationSize = 1
    )
    @Column(name = "batch_id")
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String batchNumber;

    private LocalDate expiryDate;

    @OneToOne(mappedBy = "batch")
    private ItemEntity item;

    @OneToMany(mappedBy = "batch")
    private List<LocationBatchEntity> locations;

    @OneToMany(mappedBy = "batch")
    private List<AttributeLinkEntity> attributeLinks;

    @OneToMany(mappedBy = "batch")
    private List<KitItemEntity> kitItems;

    @OneToMany(mappedBy = "batch")
    private List<StockEntity> stock;
}
