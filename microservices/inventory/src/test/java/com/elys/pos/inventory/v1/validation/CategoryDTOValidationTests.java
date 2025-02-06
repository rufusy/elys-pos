package com.elys.pos.inventory.v1.validation;

import com.elys.pos.inventory.v1.api.model.category.Category;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CategoryDTOValidationTests {

    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenNameIsNull_thenValidationFails() {
        Category category = Category.builder().name(null).build();
        Set<ConstraintViolation<Category>> violations = validator.validate(category);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(violation -> violation.getMessage().equals("Name cannot be null")));
    }

    @Test
    public void whenNameIsBlank_thenValidationFails() {
        Category category = Category.builder().name(null).build();
        Set<ConstraintViolation<Category>> violations = validator.validate(category);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(violation -> violation.getMessage().equals("Name cannot be empty")));
    }

    @Test
    public void whenDescriptionIsNull_thenValidationFails() {
        Category category = Category.builder().description(null).build();
        Set<ConstraintViolation<Category>> violations = validator.validate(category);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(violation -> violation.getMessage().equals("Description cannot be null")));
    }

    @Test
    public void whenDescriptionIsBlank_thenValidationFails() {
        Category category = Category.builder().description("").build();
        Set<ConstraintViolation<Category>> violations = validator.validate(category);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(violation -> violation.getMessage().equals("Description cannot be empty")));
    }
}
