package com.elys.pos.inventory.v1.mapper;

import com.elys.pos.inventory.v1.api.model.Category;
import com.elys.pos.inventory.v1.entity.CategoryEntity;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mappings({
            @Mapping(target = "serviceAddress", ignore = true),
            @Mapping(target = "id", source = "id", qualifiedByName = "uuidToString"),
            @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "uuidToString"),
            @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "uuidToString"),
            @Mapping(target = "deletedBy", source = "deletedBy", qualifiedByName = "uuidToString"),
            @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "dateTimeToString"),
            @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "dateTimeToString"),
            @Mapping(target = "deletedAt", source = "deletedAt", qualifiedByName = "dateTimeToString"),
    })
    Category entityToApi(CategoryEntity entity);

    @Mappings({
            @Mapping(target = "version", ignore = true),
            @Mapping(target = "id", source = "id", qualifiedByName = "stringToUuid"),
            @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "stringToUuid"),
            @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "stringToUuid"),
            @Mapping(target = "deletedBy", source = "deletedBy", qualifiedByName = "stringToUuid"),
            @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "stringToDateTime"),
            @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "stringToDateTime"),
            @Mapping(target = "deletedAt", source = "deletedAt", qualifiedByName = "stringToDateTime")
    })
    CategoryEntity apiToEntity(Category api);

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
