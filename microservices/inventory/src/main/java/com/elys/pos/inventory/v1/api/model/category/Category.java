package com.elys.pos.inventory.v1.api.model.category;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private String id;
    private String name;
    private String description;
    private ParentCategory parentCategory;
    private String createdBy;
    private String createdAt;
    private String updatedBy;
    private String updatedAt;
    private boolean deleted;
    private String deletedBy;
    private String deletedAt;
    private String serviceAddress;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParentCategory {
        private String name;
    }
}
