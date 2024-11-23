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
@Table(name = "units_of_measure")
public class UnitOfMeasureEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(length = 10)
    private String abbreviation;

    @Column(length = 32)
    private String type;

    @OneToMany(mappedBy = "unitOfMeasure", fetch = FetchType.LAZY)
    private List<ReceivingItemEntity> receivedItems;
}
