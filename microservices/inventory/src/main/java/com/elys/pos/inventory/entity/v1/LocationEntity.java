package com.elys.pos.inventory.entity.v1;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "locations")
public class LocationEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(length = 32, nullable = false)
    private String name;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    private List<AttributeLinkEntity> attributeLinks;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    private List<ReceivingEntity> receivings;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    private List<LocationBatchEntity> batches;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    private List<StockEntity> stock;
}
