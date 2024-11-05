package com.elys.pos.inventory.repository.v1;

import com.elys.pos.inventory.entity.v1.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, UUID>, JpaSpecificationExecutor<ItemEntity>,
        PagingAndSortingRepository<ItemEntity, UUID> {

    @Transactional(readOnly = true)
    @Query("SELECT e FROM ItemEntity e WHERE e.deleted = false")
    List<ItemEntity> findAllActive();

    @Modifying
    @Transactional
    @Query("UPDATE ItemEntity e SET e.deleted = true, e.deletedBy = :deletedBy, e.deletedAt = :deletedAt WHERE e.id = :id")
    void softDelete(@Param("id") UUID id,
                    @Param("deletedBy") Long deletedBy,
                    @Param("deletedAt") LocalDateTime deletedAt);
}
