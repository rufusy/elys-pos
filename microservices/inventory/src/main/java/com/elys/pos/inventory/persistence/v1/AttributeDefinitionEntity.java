package com.elys.pos.inventory.persistence.v1;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "attribute_definitions")
public class AttributeDefinitionEntity extends BaseEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "attribute_definitions_sequence"
    )
    @SequenceGenerator(
            name = "attribute_definitions_sequence",
            sequenceName = "attribute_definitions_sequence",
            allocationSize = 1
    )
    @Column(name = "definition_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String definitionName;

    @Column(nullable = false, length = 45)
    private String definitionType;

    @Column(length = 32)
    private String definitionUnit;

    private short definitionFlags;

    @ManyToOne
    @JoinColumn(name = "parent_definition_id")
    private AttributeDefinitionEntity parentDefinition;

    @OneToMany(mappedBy = "attributeDefinition")
    private List<AttributeLinkEntity> attributeLinks;
}
