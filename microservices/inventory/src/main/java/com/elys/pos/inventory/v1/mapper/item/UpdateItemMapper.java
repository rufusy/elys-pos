package com.elys.pos.inventory.v1.mapper.item;

import com.elys.pos.inventory.v1.api.model.item.UpdateItem;
import com.elys.pos.inventory.v1.entity.ItemEntity;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UpdateItemMapper {

    @Mappings({
            @Mapping(target = "id", source = "id", qualifiedByName = "stringToUuid"),
            @Mapping(target = "supplierId", source = "supplierId", qualifiedByName = "stringToUuid"),
            @Mapping(target = "taxCategoryId", source = "taxCategoryId", qualifiedByName = "stringToUuid"),
    })
    ItemEntity apiToEntity(UpdateItem api);

    @Named("stringToUuid")
    default UUID stringToUuid(String uuidString) {
        return uuidString != null ? UUID.fromString(uuidString) : null;
    }
}
