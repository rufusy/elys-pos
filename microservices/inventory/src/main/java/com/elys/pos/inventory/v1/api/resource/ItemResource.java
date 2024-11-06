package com.elys.pos.inventory.v1.api.resource;

import com.elys.pos.inventory.v1.api.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface ItemResource {

    @GetMapping(
            value = "/items",
            produces = "application/json")
    ResponseEntity<Page<Item>> getItems(
            @RequestParam(required = false, defaultValue = "") String param,
            @RequestParam(required = false, defaultValue = "") String sort,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size);
}
