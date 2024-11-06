package com.elys.pos.inventory.v1.repository;

import com.elys.pos.inventory.v1.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TagRepository extends JpaRepository<TagEntity, UUID> {
}
