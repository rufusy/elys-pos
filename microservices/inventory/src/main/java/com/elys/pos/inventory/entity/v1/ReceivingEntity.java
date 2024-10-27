package com.elys.pos.inventory.entity.v1;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "receivings")
public class ReceivingEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, length = 32)
    private String reference;

    @Column(columnDefinition = "TEXT")
    private String comment;

    private Long supplierId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private LocationEntity location;

    @OneToMany(mappedBy = "receiving", fetch = FetchType.LAZY)
    private List<AttributeLinkEntity> attributeLinks;

    @OneToMany(mappedBy = "receiving", fetch = FetchType.LAZY)
    private List<ReceivingItemEntity> items;
}
