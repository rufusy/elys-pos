package com.elys.pos.inventory.v1.repository;

import com.elys.pos.inventory.v1.entity.ItemTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemTypeRepository extends JpaRepository<ItemTypeEntity, UUID> {
}
