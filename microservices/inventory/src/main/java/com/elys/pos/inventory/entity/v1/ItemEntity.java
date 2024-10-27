package com.elys.pos.inventory.entity.v1;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "items")
public class ItemEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be empty")
    @Size(max = 255, message = "Name must be less than 255 characters")
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull(message = "Category cannot be null")
    @NotBlank(message = "Category cannot be empty")
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @NotNull(message = "Description cannot be null")
    @NotBlank(message = "Description cannot be empty")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(unique = true)
    private String itemNumber;
    
    @Builder.Default
    @NotNull(message = "Reorder level cannot be null")
    @DecimalMin(value = "0.00", message = "Reorder level must be at least 0")
    @Digits(integer = 13, fraction = 2, message = "Reorder level must have at most 15 total digits, with 2 decimal places")
    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal reorderLevel = BigDecimal.ZERO;

    private String imageUrl;

    private UUID supplierId;

    private UUID taxCategoryId;

    @NotNull(message = "HSN code cannot be null")
    @NotBlank(message = "HSN code cannot be empty")
    @Size(max = 32, message = "Name must be less than 32 characters")
    @Column(length = 32, nullable = false)
    private String hsnCode;

    @NotNull(message = "Item type cannot be null")
    @NotBlank(message = "Item type cannot be empty")
    @ManyToOne
    @JoinColumn(name = "item_type_id", nullable = false)
    private ItemTypeEntity itemType;

    @NotNull(message = "Stock type cannot be null")
    @NotBlank(message = "Stock type cannot be empty")
    @ManyToOne
    @JoinColumn(name = "stock_type_id", nullable = false)
    private StockTypeEntity stockType;

    @NotNull(message = "Serialized cannot be null")
    @NotBlank(message = "Serialized cannot be empty")
    @Builder.Default
    @Column(nullable = false)
    private boolean serialized = false;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<AttributeLinkEntity> attributeLinks = Collections.emptyList();

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<KitItemEntity> kits = Collections.emptyList();

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<ReceivingItemEntity> received = Collections.emptyList();

    @ManyToMany
    @JoinTable(
            name = "items_tags",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<TagEntity> tags = Collections.emptyList();
}
