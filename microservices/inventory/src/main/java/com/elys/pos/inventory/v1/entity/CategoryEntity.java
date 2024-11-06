package com.elys.pos.inventory.v1.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
