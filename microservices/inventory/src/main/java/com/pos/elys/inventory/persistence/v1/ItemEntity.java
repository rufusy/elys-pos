package com.pos.elys.inventory.persistence.v1;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "items")
public class ItemEntity extends BaseEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "items_sequence"
    )
    @SequenceGenerator(
            name = "items_sequence",
            sequenceName = "items_sequence",
            allocationSize = 1
    )
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @Column(nullable = false)
    private String description;

    @Column(unique = true)
    private String itemNumber;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal reorderLevel = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal receivingQuantity = BigDecimal.ZERO;

    private String picFilename;

    private Integer supplierId;

    private Integer taxCategoryId;

    @Column(length = 32, nullable = false)
    private String hsnCode;

    @Column(nullable = false)
    private boolean itemType = false;

    @Column(nullable = false)
    private boolean stockType = false;

    @Column(nullable = false)
    private boolean isSerialized = false;

    @OneToMany(mappedBy = "item")
    private List<AttributeLinkEntity> attributeLinks;
}
