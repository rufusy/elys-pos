package com.elys.pos.inventory.v1.mapper.item;

import com.elys.pos.inventory.v1.api.model.item.Item;
import com.elys.pos.inventory.v1.entity.ItemEntity;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemMapper {

    @Mappings({
            @Mapping(target = "serviceAddress", ignore = true),
            @Mapping(target = "id", source = "id", qualifiedByName = "uuidToString"),
            @Mapping(target = "supplierId", source = "supplierId", qualifiedByName = "uuidToString"),
            @Mapping(target = "taxCategoryId", source = "taxCategoryId", qualifiedByName = "uuidToString"),
            @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "dateTimeToString"),
            @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "dateTimeToString"),
            @Mapping(target = "deletedAt", source = "deletedAt", qualifiedByName = "dateTimeToString"),
    })
    Item entityToApi(ItemEntity entity);

    @Named("uuidToString")
    default String uuidToString(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }

    @Named("stringToDateTime")
    default LocalDateTime stringToDateTime(String date) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        return LocalDate.parse(date).atStartOfDay();
    }

    @Named("dateTimeToString")
    default String dateTimeToString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.toLocalDate().toString();
    }
}
