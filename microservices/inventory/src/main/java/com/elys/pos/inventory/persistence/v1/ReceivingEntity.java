package com.elys.pos.inventory.persistence.v1;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "receivings")
public class ReceivingEntity extends BaseEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "receivings_sequence"
    )
    @SequenceGenerator(
            name = "receivings_sequence",
            sequenceName = "receivings_sequence",
            allocationSize = 1
    )
    @Column(name = "receiving_id")
    private Long id;

    @Column(nullable = false, length = 32)
    private String reference;

    @Column(columnDefinition = "TEXT")
    private String comment;

    private Long supplierId;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private LocationEntity location;

    @OneToMany(mappedBy = "receiving")
    private List<AttributeLinkEntity> attributeLinks;

    @OneToMany(mappedBy = "receiving")
    private List<ReceivingItemEntity> items;
}
