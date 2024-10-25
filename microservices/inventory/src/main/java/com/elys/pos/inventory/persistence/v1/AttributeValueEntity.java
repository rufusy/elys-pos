package com.elys.pos.inventory.persistence.v1;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "attribute_values")
public class AttributeValueEntity extends BaseEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "attribute_values_sequence"
    )
    @SequenceGenerator(
            name = "attribute_values_sequence",
            sequenceName = "attribute_values_sequence",
            allocationSize = 1
    )
    @Column(name = "attribute_id")
    private Long id;

    private String attributeValue;

    private LocalDate attributeDate;

    @Column(precision = 7, scale = 3, columnDefinition = "decimal(7,3)")
    private BigDecimal attributeDecimal;

    @OneToMany(mappedBy = "attributeValue")
    private List<AttributeLinkEntity> attributeLinks;
}
