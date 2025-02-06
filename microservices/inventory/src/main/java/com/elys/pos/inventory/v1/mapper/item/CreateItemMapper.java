package com.elys.pos.inventory.v1.mapper.item;

import com.elys.pos.inventory.v1.api.model.item.CreateItem;
import com.elys.pos.inventory.v1.entity.ItemEntity;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CreateItemMapper {

    @Mappings({
            @Mapping(target = "supplierId", source = "supplierId", qualifiedByName = "stringToUuid"),
            @Mapping(target = "taxCategoryId", source = "taxCategoryId", qualifiedByName = "stringToUuid"),
    })
    ItemEntity apiToEntity(CreateItem api);

    @Named("stringToUuid")
    default UUID stringToUuid(String uuidString) {
        return uuidString != null ? UUID.fromString(uuidString) : null;
    }
}
