package com.elys.pos.inventory.v1.api.resource;

import com.elys.pos.inventory.v1.api.model.category.Category;
import com.elys.pos.inventory.v1.api.model.category.CreateCategory;
import com.elys.pos.inventory.v1.api.model.category.UpdateCategory;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequestMapping("/v1")
public interface CategoryResource {

    /**
     * Sample usage:
     * curl $HOST:$PORT/categories?param=%7B%22name%22%3A%22cat1%22%7D&sort=name:desc&page=0&size=10
     *
     * @param filter url encoded param to search categories e.g. {"name":"cat1"}
     * @param sort   param to order categories
     * @param page   page number requested
     * @param size   number of categories requested per page
     * @return categories in the page
     */
    @GetMapping(value = "/categories", produces = "application/json")
    ResponseEntity<Page<Category>> getCategories(
            @RequestParam(value = "filter", required = false, defaultValue = "") String filter,
            @RequestParam(value = "page", required = false, defaultValue = "") String sort,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size);

    /**
     * Sample usage:
     * curl $HOST:$PORT/categories/3657f25f-a06d-4851-9260-2e7b8f44cd74
     *
     * @param categoryId id of the category
     * @return category if found, else null
     */
    @GetMapping(value = "/categories/{categoryId}", produces = "application/json")
    ResponseEntity<Mono<Category>> getCategoryById(@PathVariable(value = "categoryId")
                                                   @UUID(message = "Invalid format for category id") String categoryId);

    /**
     * Sample usage:
     * curl -X POST $HOST:$PORT/categories -H "Content-Type:application/json" -d "{...body...}"
     *
     * @param body a JSON representation of the new category
     * @return a JSON representation of the newly created category
     */
    @PostMapping(value = "/categories", consumes = "application/json", produces = "application/json")
    ResponseEntity<Mono<Category>> createCategory(@RequestBody @Valid CreateCategory body);

    /**
     * Sample usage:
     * curl -X UPDATE $HOST:$PORT/categories -H "Content-Type:application/json" -d "{...body...}"
     *
     * @param body a JSON representation of the updated category
     * @return a JSON representation of the updated category
     */
    @PutMapping(value = "/categories", consumes = "application/json", produces = "application/json")
    ResponseEntity<Mono<Category>> updateCategory(@RequestBody @Valid UpdateCategory body);

    /**
     * Sample usage:
     * curl -X DELETE $HOST:$PORT/categories/3657f25f-a06d-4851-9260-2e7b8f44cd74
     *
     * @param categoryId category id
     */
    @DeleteMapping(value = "/categories/{categoryId}")
    ResponseEntity<Mono<Void>> deleteItem(@PathVariable(value = "categoryId")
                                          @UUID(message = "Invalid format for category id") String categoryId);
}
