package com.elys.pos.inventory.v1.mapper;

import com.elys.pos.inventory.v1.api.model.Item;
import com.elys.pos.inventory.v1.entity.ItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mappings({
            @Mapping(target = "serviceAddress", ignore = true),
            @Mapping(target = "id", source = "id", qualifiedByName = "uuidToString"),
            @Mapping(target = "supplierId", source = "supplierId", qualifiedByName = "uuidToString"),
            @Mapping(target = "taxCategoryId", source = "taxCategoryId", qualifiedByName = "uuidToString"),
            @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "uuidToString"),
            @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "uuidToString"),
            @Mapping(target = "deletedBy", source = "deletedBy", qualifiedByName = "uuidToString"),
            @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "dateTimeToString"),
            @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "dateTimeToString"),
            @Mapping(target = "deletedAt", source = "deletedAt", qualifiedByName = "dateTimeToString"),
    })
    Item entityToApi(ItemEntity entity);

    @Mappings({
            @Mapping(target = "version", ignore = true),
            @Mapping(target = "id", source = "id", qualifiedByName = "stringToUuid"),
            @Mapping(target = "supplierId", source = "supplierId", qualifiedByName = "stringToUuid"),
            @Mapping(target = "taxCategoryId", source = "taxCategoryId", qualifiedByName = "stringToUuid"),
            @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "stringToUuid"),
            @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "stringToUuid"),
            @Mapping(target = "deletedBy", source = "deletedBy", qualifiedByName = "stringToUuid"),
            @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "stringToDateTime"),
            @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "stringToDateTime"),
            @Mapping(target = "deletedAt", source = "deletedAt", qualifiedByName = "stringToDateTime")
    })
    ItemEntity apiToEntity(Item api);

    @Named("uuidToString")
    default String uuidToString(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }

    @Named("stringToUuid")
    default UUID stringToUuid(String uuidString) {
        return uuidString != null ? UUID.fromString(uuidString) : null;
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
