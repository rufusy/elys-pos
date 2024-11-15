package com.elys.pos.inventory.v1.api.resource;

import com.elys.pos.inventory.v1.api.model.Item;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

public interface ItemResource {

    /**
     * Sample usage:
     * curl $HOST:$PORT/items?param=%7B%22name%22%3A%22item%22%7D&sort=name:desc,itemNumber:asc&page=0&size=10
     *
     * @param filter url encoded param to search items e.g. {"name":"item"}
     * @param sort   param to order items
     * @param page   page number requested
     * @param size   number of items requested per page
     * @return items in the page
     */
    @GetMapping(
            value = "/items",
            produces = "application/json")
    ResponseEntity<Page<Item>> getItems(
            @RequestParam(value = "filter", required = false, defaultValue = "") String filter,
            @RequestParam(value = "sort", required = false, defaultValue = "") String sort,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size);

    /**
     * Sample usage:
     * curl $HOST:$PORT/items/3657f25f-a06d-4851-9260-2e7b8f44cd74
     *
     * @param itemId id of the item
     * @return item if found, else null
     */
    @GetMapping(value = "/items/{itemId}",
            produces = "application/json")
    ResponseEntity<Mono<Item>> getItemById(
            @PathVariable(value = "itemId")
            @Pattern(
                    regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$",
                    message = "Invalid format for item id"
            )
            String itemId);

    /**
     * Sample usage:
     * curl -X POST $HOST:$PORT/items -H "Content-Type:application/json" \
     * -d "{...body...}"
     *
     * @param body a JSON representation of the new item
     * @return a JSON representation of the newly created item
     */
    @PostMapping(
            value = "/items",
            consumes = "application/json",
            produces = "application/json"
    )
    ResponseEntity<Mono<Item>> createItem(@RequestBody @Valid Item body);

    /**
     * Sample usage:
     * curl -X UPDATE $HOST:$PORT/items -H "Content-Type:application/json" \
     * -d "{...body...}"
     *
     * @param body a JSON representation of the updated item
     * @return a JSON representation of the updated item
     */
    @PutMapping(
            value = "/items",
            consumes = "application/json",
            produces = "application/json"
    )
    ResponseEntity<Mono<Item>> updateItem(@RequestBody @Valid Item body);

    /**
     * Sample usage:
     * curl -X DELETE $HOST:$PORT/items/3657f25f-a06d-4851-9260-2e7b8f44cd74
     *
     * @param itemId item id
     */
    @DeleteMapping(value = "/items/{itemId}")
    ResponseEntity<Mono<Void>> deleteItem(
            @PathVariable(value = "itemId")
            @Pattern(
                    regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$",
                    message = "Invalid format for item id"
            )
            String itemId);
}
