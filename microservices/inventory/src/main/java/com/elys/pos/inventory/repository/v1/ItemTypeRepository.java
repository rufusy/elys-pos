package com.elys.pos.inventory.repository.v1;

import com.elys.pos.inventory.entity.v1.ItemTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemTypeRepository extends JpaRepository<ItemTypeEntity, UUID> {
}
