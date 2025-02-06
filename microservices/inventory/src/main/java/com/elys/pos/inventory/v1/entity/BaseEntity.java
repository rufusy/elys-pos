package com.elys.pos.inventory.v1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass
public class BaseEntity {

    @Version
    private Integer version;

    @Column(updatable = false, nullable = false)
    private String createdBy;

    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(insertable = false)
    private String updatedBy;

    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @Column(insertable = false)
    private String deletedBy;

    @Column(insertable = false)
    private LocalDateTime deletedAt;

    @Builder.Default
    @Column(nullable = false)
    private boolean deleted = false;
}
