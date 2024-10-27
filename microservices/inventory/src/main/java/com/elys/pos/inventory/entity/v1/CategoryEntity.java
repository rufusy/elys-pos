package com.elys.pos.inventory.entity.v1;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Table(name = "categories")
public class CategoryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "Name cannot be empty")
    @NotNull(message = "Name cannot be null")
    @Size(max = 255, message = "Name must be less than 255 characters")
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank(message = "Description cannot be empty")
    @NotNull(message = "Description cannot be null")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    private CategoryEntity parentCategory;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<ItemEntity> items = Collections.emptyList();

    @OneToMany(mappedBy = "category")
    private List<AttributeLinkEntity> attributeLinks = Collections.emptyList();

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<KitEntity> kits = Collections.emptyList();
}
