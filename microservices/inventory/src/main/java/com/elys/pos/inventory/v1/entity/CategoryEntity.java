package com.elys.pos.inventory.v1.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "categories", uniqueConstraints = @UniqueConstraint(name = "unique_category_name", columnNames = "name"))
public class CategoryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    private CategoryEntity parentCategory;

    @Builder.Default
    @OneToMany(mappedBy = "category")
    private List<ItemEntity> items = Collections.emptyList();

    @Builder.Default
    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private List<AttributeLinkEntity> attributeLinks = Collections.emptyList();

    @Builder.Default
    @OneToMany(mappedBy = "category")
    private List<KitEntity> kits = Collections.emptyList();
}
