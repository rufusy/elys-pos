package com.elys.pos.inventory.entity.v1;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "kits")
public class KitEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @Column(nullable = false)
    private String description;

    @Column(unique = true)
    private String kitNumber;

    @Builder.Default
    @Column(nullable = false)
    private boolean isPriceOverride = false;

    @OneToMany(mappedBy = "kit", fetch = FetchType.LAZY)
    private List<AttributeLinkEntity> attributeLinks;

    @OneToMany(mappedBy = "kit", fetch = FetchType.LAZY)
    private List<KitItemEntity> items;
}
