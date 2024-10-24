package com.pos.elys.inventory.persistence.v1;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "attribute_links")
public class AttributeLinkEntity extends BaseEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "attribute_links_sequence"
    )
    @SequenceGenerator(
            name = "attribute_links_sequence",
            sequenceName = "attribute_links_sequence",
            allocationSize = 1
    )
    @Column(name = "link_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "attribute_id", nullable = false)
    private AttributeValueEntity attributeValue;

    @ManyToOne
    @JoinColumn(name = "definition_id", nullable = false)
    private AttributeDefinitionEntity attributeDefinition;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
}
