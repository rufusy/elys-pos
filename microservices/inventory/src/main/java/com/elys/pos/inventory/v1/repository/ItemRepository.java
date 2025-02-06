package com.elys.pos.inventory.v1.repository;

import com.elys.pos.inventory.v1.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, UUID>, PagingAndSortingRepository<ItemEntity, UUID>,
        JpaSpecificationExecutor<ItemEntity> {

    @Modifying
    @Transactional
    @Query("UPDATE ItemEntity e SET e.deleted = true, e.deletedBy = :deletedBy, e.deletedAt = :deletedAt WHERE e.id = :id")
    void flagAsDeleted(@Param("id") UUID id, @Param("deletedBy") String deletedBy, @Param("deletedAt") LocalDateTime deletedAt);
}
