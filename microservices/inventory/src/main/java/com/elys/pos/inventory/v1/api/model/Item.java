package com.elys.pos.inventory.v1.api.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private UUID id;

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be empty")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @NotNull(message = "Description cannot be null")
    @NotBlank(message = "Description cannot be empty")
    private String description;

    @Size(max = 255, message = "Item number must be less than 255 characters")
    private String itemNumber;

    @Size(max = 255, message = "Image url must be less than 255 characters")
    private String imageUrl;

//    @Pattern(
//            regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$",
//            message = "Invalid UUID format for supplierId"
//    )
    private UUID supplierId;

//    @Pattern(
//            regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$",
//            message = "Invalid UUID format for taxCategoryId"
//    )
    private UUID taxCategoryId;

    @NotNull(message = "HSN code cannot be null")
    @NotBlank(message = "HSN code cannot be empty")
    @Size(max = 32, message = "Name must be less than 32 characters")
    private String hsnCode;

    @NotNull(message = "Selling price cannot be null")
    @DecimalMin(value = "0.00", inclusive = true, message = "Selling price must be at least 0")
    @Digits(integer = 13, fraction = 2, message = "Selling price must have at most 15 total digits, with 2 decimal places")
    private BigDecimal sellingPrice;

    @NotNull(message = "Serialized cannot be null")
    private boolean serialized;

    @NotNull(message = "Batch tracked cannot be null")
    private boolean batchTracked;

//    @Pattern(
//            regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$",
//            message = "Invalid UUID format for createdBy"
//    )
    private UUID createdBy;

//    @Pattern(
//            regexp = "^\\d{4}-\\d{2}-\\d{2}$",
//            message = "Invalid date format for createdAt. Expected YYYY-MM-DD"
//    )
    private LocalDateTime createdAt;

//    @Pattern(
//            regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$",
//            message = "Invalid UUID format for updatedBy"
//    )
    private UUID updatedBy;

//    @Pattern(
//            regexp = "^\\d{4}-\\d{2}-\\d{2}$",
//            message = "Invalid date format for updatedAt. Expected YYYY-MM-DD"
//    )
    private LocalDateTime updatedAt;

    private boolean deleted;

//    @Pattern(
//            regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$",
//            message = "Invalid UUID format for deletedBy"
//    )
    private UUID deletedBy;

//    @Pattern(
//            regexp = "^\\d{4}-\\d{2}-\\d{2}$",
//            message = "Invalid date format for deletedAt. Expected YYYY-MM-DD"
//    )
    private LocalDateTime deletedAt;

    private String serviceAddress;

    @Valid
    @NotNull(message = "Category type cannot be null")
    private Category category;
    @Valid
    @NotNull(message = "Item type cannot be null")
    private ItemType itemType;
    @Valid
    @NotNull(message = "Stock type cannot be null")
    private StockType stockType;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category {
        @NotNull(message = "Category name cannot be null")
        @NotBlank(message = "Category name cannot be empty")
        private String name;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemType {
        @NotNull(message = "Item type name cannot be null")
        @NotBlank(message = "Item type name cannot be empty")
        private String name;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockType {
        @NotNull(message = "Stock type name cannot be null")
        @NotBlank(message = "Stock type name cannot be empty")
        private String name;
    }
}
