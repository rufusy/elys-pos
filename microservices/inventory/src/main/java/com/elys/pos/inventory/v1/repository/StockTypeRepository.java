package com.elys.pos.inventory.v1.repository;

import com.elys.pos.inventory.v1.entity.StockTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface StockTypeRepository extends JpaRepository<StockTypeEntity, UUID>,
        PagingAndSortingRepository<StockTypeEntity, UUID> {
    Optional<StockTypeEntity> findByName(String name);
}
