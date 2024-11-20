package com.elys.pos.inventory.v1.repository;

import com.elys.pos.inventory.v1.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID>,
        PagingAndSortingRepository<CategoryEntity, UUID>, JpaSpecificationExecutor<CategoryEntity> {

//    @Transactional(readOnly = true)
//    @Query("SELECT c FROM CategoryEntity c WHERE c.deleted = false")
//    List<CategoryEntity> findAllActive();

//    @Modifying
//    @Transactional
//    @Query("UPDATE CategoryEntity c SET c.deleted = true, c.deletedBy = :deletedBy, c.deletedAt = :deletedAt WHERE c.id = :id")
//    void softDelete(@Param("id") UUID id,
//                    @Param("deletedBy") Long deletedBy,
//                    @Param("deletedAt") LocalDateTime deletedAt);

    Optional<CategoryEntity> findByName(String name);

    @Modifying
    @Transactional
    @Query("UPDATE CategoryEntity e SET e.deleted = true, e.deletedBy = :deletedBy, e.deletedAt = :deletedAt WHERE e.id = :id")
    void flagAsDeleted(@Param("id") UUID id, @Param("deletedBy") UUID deletedBy, @Param("deletedAt") LocalDateTime deletedAt);
}
