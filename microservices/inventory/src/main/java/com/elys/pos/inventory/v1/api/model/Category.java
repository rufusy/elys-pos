package com.elys.pos.inventory.v1.api.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Category {

    @org.hibernate.validator.constraints.UUID(message = "Invalid format for category id")
    private String id;

    @NotBlank(message = "Name cannot be empty")
    @NotNull(message = "Name cannot be null")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @NotBlank(message = "Description cannot be empty")
    @NotNull(message = "Description cannot be null")
    private String description;

    @Valid
    private ParentCategory parentCategory;

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

    @Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ParentCategory {
        @Size(max = 255, message = "Parent name must be less than 255 characters")
        private String name;
    }
}
