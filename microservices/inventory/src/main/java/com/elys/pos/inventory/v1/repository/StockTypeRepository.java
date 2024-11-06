package com.elys.pos.inventory.v1.repository;

import com.elys.pos.inventory.v1.entity.StockTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StockTypeRepository extends JpaRepository<StockTypeEntity, UUID> {
}
