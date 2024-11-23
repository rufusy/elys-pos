package com.elys.pos.inventory.v1.api.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockType {
    private String name;
}
