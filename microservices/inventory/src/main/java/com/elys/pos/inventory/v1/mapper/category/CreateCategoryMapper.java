package com.elys.pos.inventory.v1.mapper.category;

import com.elys.pos.inventory.v1.api.model.category.CreateCategory;
import com.elys.pos.inventory.v1.entity.CategoryEntity;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CreateCategoryMapper {

    CategoryEntity apiToEntity(CreateCategory api);
}
