package com.elys.pos.inventory.entity.v1;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "batches")
public class BatchEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(unique = true, nullable = false, updatable = false)
    private String number;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity item;

    private LocalDate expiryDate;

    @OneToOne(mappedBy = "batch")
    private ReceivingItemEntity receivedItem;

    @OneToMany(mappedBy = "batch", fetch = FetchType.LAZY)
    private List<LocationBatchEntity> locations;

    @OneToMany(mappedBy = "batch", fetch = FetchType.LAZY)
    private List<AttributeLinkEntity> attributeLinks;

    @OneToMany(mappedBy = "batch", fetch = FetchType.LAZY)
    private List<KitItemEntity> kitItems;

    @OneToMany(mappedBy = "batch", fetch = FetchType.LAZY)
    private List<StockEntity> stock;
}
