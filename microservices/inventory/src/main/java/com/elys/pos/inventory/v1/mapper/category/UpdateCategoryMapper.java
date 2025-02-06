package com.elys.pos.inventory.v1.mapper.category;

import com.elys.pos.inventory.v1.api.model.category.UpdateCategory;
import com.elys.pos.inventory.v1.entity.CategoryEntity;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UpdateCategoryMapper {

    @Mappings({
            @Mapping(target = "id", source = "id", qualifiedByName = "stringToUuid"),
    })
    CategoryEntity apiToEntity(UpdateCategory api);

    @Named("stringToUuid")
    default UUID stringToUuid(String uuidString) {
        return uuidString != null ? UUID.fromString(uuidString) : null;
    }
}
