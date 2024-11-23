package com.elys.pos.inventory.v1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "attribute_definitions")
public class AttributeDefinitionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, length = 45)
    private String type;

    @Column(length = 32)
    private String unit;

    private short flags;

    @ManyToOne
    @JoinColumn(name = "parent_definition_id")
    private AttributeDefinitionEntity parentDefinition;

    @OneToMany(mappedBy = "definition", fetch = FetchType.LAZY)
    private List<AttributeLinkEntity> attributeLinks;
}
