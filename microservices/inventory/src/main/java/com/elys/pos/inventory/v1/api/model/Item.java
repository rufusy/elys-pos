package com.elys.pos.inventory.v1.api.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Item {

    @org.hibernate.validator.constraints.UUID(message = "Invalid format for item id")
    private String id;

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

    @org.hibernate.validator.constraints.UUID(message = "Invalid format for supplier id")
    private String supplierId;

    @org.hibernate.validator.constraints.UUID(message = "Invalid format for tax category id")
    private String taxCategoryId;

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

    @org.hibernate.validator.constraints.UUID(message = "Invalid format for created by id")
    private String createdBy;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Invalid date format for created at. Expected format is YYYY-MM-DD")
    private String createdAt;

    @org.hibernate.validator.constraints.UUID(message = "Invalid format for updated by id")
    private String updatedBy;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Invalid date format for updated at. Expected format is YYYY-MM-DD")
    private String updatedAt;

    private boolean deleted;

    @org.hibernate.validator.constraints.UUID(message = "Invalid format for deleted by id")
    private String deletedBy;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Invalid date format for deleted at. Expected format is YYYY-MM-DD")
    private String deletedAt;

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

    @Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class Category {
        @NotNull(message = "Category name cannot be null")
        @NotBlank(message = "Category name cannot be empty")
        private String name;
    }

    @Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ItemType {
        @NotNull(message = "Item type name cannot be null")
        @NotBlank(message = "Item type name cannot be empty")
        private String name;
    }

    @Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class StockType {
        @NotNull(message = "Stock type name cannot be null")
        @NotBlank(message = "Stock type name cannot be empty")
        private String name;
    }
}
