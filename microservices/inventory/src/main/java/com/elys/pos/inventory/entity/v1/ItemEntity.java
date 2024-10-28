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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @NotNull(message = "Description cannot be null")
    @NotBlank(message = "Description cannot be empty")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(unique = true)
    private String itemNumber;

    private String imageUrl;

    private UUID supplierId;

    @Builder.Default
    @NotNull(message = "Selling price cannot be null")
    @DecimalMin(value = "0.00", inclusive = true, message = "Selling price must be at least 0")
    @Digits(integer = 13, fraction = 2, message = "Selling price must have at most 15 total digits, with 2 decimal places")
    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal sellingPrice = BigDecimal.ZERO;

    private UUID taxCategoryId;

    @NotNull(message = "HSN code cannot be null")
    @NotBlank(message = "HSN code cannot be empty")
    @Size(max = 32, message = "Name must be less than 32 characters")
    @Column(length = 32, nullable = false)
    private String hsnCode;

    @NotNull(message = "Item type cannot be null")
    @ManyToOne
    @JoinColumn(name = "item_type_id", nullable = false)
    private ItemTypeEntity itemType;

    @NotNull(message = "Stock type cannot be null")
    @ManyToOne
    @JoinColumn(name = "stock_type_id", nullable = false)
    private StockTypeEntity stockType;

    @NotNull(message = "Serialized cannot be null")
    @Builder.Default
    @Column(nullable = false)
    private boolean serialized = false;

    @NotNull(message = "Serialized cannot be null")
    @Column(nullable = false)
    private boolean batchTracked;

    @Builder.Default
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<AttributeLinkEntity> attributeLinks = Collections.emptyList();

    @Builder.Default
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<KitItemEntity> kits = Collections.emptyList();

    @Builder.Default
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<ReceivingItemEntity> received = Collections.emptyList();

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "items_tags",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<TagEntity> tags = Collections.emptyList();
}
