package com.elys.pos.inventory.v1.entity;

import jakarta.persistence.*;
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
@Table(name = "items", uniqueConstraints = @UniqueConstraint(name = "unique_item_name", columnNames = "name"))
public class ItemEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(unique = true)
    private String itemNumber;

    private String imageUrl;

    private UUID supplierId;

    @Builder.Default
    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal sellingPrice = BigDecimal.ZERO;

    private UUID taxCategoryId;

    @Column(length = 32, nullable = false)
    private String hsnCode;

    @ManyToOne
    @JoinColumn(name = "item_type_id", nullable = false)
    private ItemTypeEntity itemType;

    @ManyToOne
    @JoinColumn(name = "stock_type_id", nullable = false)
    private StockTypeEntity stockType;

    @Builder.Default
    @Column(nullable = false)
    private boolean serialized = false;

    @Column(nullable = false)
    private boolean batchTracked;

    @Builder.Default
    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER)
    private List<AttributeLinkEntity> attributeLinks = Collections.emptyList();

    @Builder.Default
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<KitItemEntity> kits = Collections.emptyList();

    @Builder.Default
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<ReceivingItemEntity> received = Collections.emptyList();

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "items_tags", joinColumns = @JoinColumn(name = "item_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<TagEntity> tags = Collections.emptyList();
}
