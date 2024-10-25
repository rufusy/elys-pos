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
@Table(name = "units_of_measure")
public class UnitOfMeasureEntity extends BaseEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "units_of_measure_sequence"
    )
    @SequenceGenerator(
            name = "units_of_measure_sequence",
            sequenceName = "units_of_measure_sequence",
            allocationSize = 1
    )
    @Column(name = "unit_of_measure_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(length = 10)
    private String abbreviation;

    @Column(length = 32)
    private String type;

    @OneToMany(mappedBy = "unitOfMeasure")
    private List<ReceivingItemEntity> receivedItems;
}
