package com.elys.pos.inventory.persistence.v1;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "locations")
public class LocationEntity extends BaseEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "locations_sequence"
    )
    @SequenceGenerator(
            name = "locations_sequence",
            sequenceName = "locations_sequence",
            allocationSize = 1
    )
    @Column(name = "location_id")
    private Long id;

    @Column(length = 32, nullable = false)
    private String name;

    @OneToMany(mappedBy = "location")
    private List<AttributeLinkEntity> attributeLinks;

    @OneToMany(mappedBy = "location")
    private List<ReceivingEntity> receivings;

    @OneToMany(mappedBy = "location")
    private List<LocationBatchEntity> batches;

    @OneToMany(mappedBy = "location")
    private List<StockEntity> stock;
}
