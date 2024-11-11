package com.elys.pos.inventory.v1.repository;

import com.elys.pos.inventory.v1.entity.ItemTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface ItemTypeRepository extends JpaRepository<ItemTypeEntity, UUID>,
        PagingAndSortingRepository<ItemTypeEntity, UUID> {
    Optional<ItemTypeEntity> findByName(String name);
}
