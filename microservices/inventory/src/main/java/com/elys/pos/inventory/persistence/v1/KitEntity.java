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
@Table(name = "kits")
public class KitEntity extends BaseEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "kits_sequence"
    )
    @SequenceGenerator(
            name = "kits_sequence",
            sequenceName = "kits_sequence",
            allocationSize = 1
    )
    @Column(name = "kit_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @Column(nullable = false)
    private String description;

    @Column(unique = true)
    private String kitNumber;

    @Column(nullable = false)
    private boolean isPriceOverride = false;

    @OneToMany(mappedBy = "kit")
    private List<AttributeLinkEntity> attributeLinks;

    @OneToMany(mappedBy = "kit")
    private List<KitItemEntity> items;
}
