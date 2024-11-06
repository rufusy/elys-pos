package com.elys.pos.inventory.v1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "attribute_values")
public class AttributeValueEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID id;

    private String value;

    private LocalDate date;

    @Column(precision = 7, scale = 3, columnDefinition = "decimal(7,3)")
    private BigDecimal decimal;

    @OneToMany(mappedBy = "value", fetch = FetchType.LAZY)
    private List<AttributeLinkEntity> attributeLinks;
}
