package com.elys.pos.inventory.repository.v1;

import com.elys.pos.inventory.entity.v1.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TagRepository extends JpaRepository<TagEntity, UUID> {
}
