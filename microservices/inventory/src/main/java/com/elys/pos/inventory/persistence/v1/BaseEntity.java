package com.elys.pos.inventory.persistence.v1;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.*;
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

    @Builder.Default
    @Column(insertable = false, nullable = false)
    private boolean deleted = false;
}
