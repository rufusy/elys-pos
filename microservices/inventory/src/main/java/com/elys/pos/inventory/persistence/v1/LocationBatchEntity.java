package com.elys.pos.inventory.persistence.v1;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "locations_batches", indexes = {
        @Index(name = "locations_batches_unique", unique = true, columnList = "location_id,batch_id")
})
public class LocationBatchEntity extends BaseEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "locations_batches_sequence"
    )
    @SequenceGenerator(
            name = "locations_batches_sequence",
            sequenceName = "locations_batches_sequence",
            allocationSize = 1
    )
    @Column(name = "location_batch_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private LocationEntity location;

    @ManyToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private BatchEntity batch;

    @Builder.Default
    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal quantity = BigDecimal.ZERO;
}
