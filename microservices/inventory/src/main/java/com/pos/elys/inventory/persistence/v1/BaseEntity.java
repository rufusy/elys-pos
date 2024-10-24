package com.pos.elys.inventory.persistence.v1;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

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
    private Long createdBy;

    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(insertable = false)
    private Long updatedBy;

    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @Column(insertable = false)
    private Long deletedBy;

    @Column(insertable = false)
    private LocalDateTime deletedAt;

    @Column(insertable = false, nullable = false)
    private boolean deleted = false;
}