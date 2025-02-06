package com.elys.pos.inventory.v1.api.model.category;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategory {

    @NotNull(message = "Id cannot be null")
    @NotBlank(message = "Id cannot be empty")
    @org.hibernate.validator.constraints.UUID(message = "Invalid format for item id")
    private String id;

    @NotBlank(message = "Name cannot be empty")
    @NotNull(message = "Name cannot be null")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @NotBlank(message = "Description cannot be empty")
    @NotNull(message = "Description cannot be null")
    private String description;

    @Valid
    private Category.ParentCategory parentCategory;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParentCategory {
        @Size(max = 255, message = "Parent name must be less than 255 characters")
        private String name;
    }
}
