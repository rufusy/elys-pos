package com.elys.pos.inventory.v1.mapper;

import com.elys.pos.inventory.v1.api.model.Item;
import com.elys.pos.inventory.v1.entity.ItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    @Mappings({
            @Mapping(target = "serviceAddress", ignore = true)
    })
    Item entityToApi(ItemEntity entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true)
    })
    ItemEntity apiToEntity(Item api);

    void updateEntityFromApi(Item api, @MappingTarget ItemEntity entity);
}
