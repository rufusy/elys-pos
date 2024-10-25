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
@Table(name = "packaging_types")
public class PackagingTypeEntity extends BaseEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "packaging_sequence"
    )
    @SequenceGenerator(
            name = "packaging_sequence",
            sequenceName = "packaging_sequence",
            allocationSize = 1
    )
    @Column(name = "packaging_type_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @OneToMany(mappedBy = "packagingType")
    private List<ReceivingItemEntity> receivedItems;
}
