package com.elys.pos.inventory.repository.v1;

import com.elys.pos.inventory.entity.v1.StockTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StockTypeRepository extends JpaRepository<StockTypeEntity, UUID> {
}
