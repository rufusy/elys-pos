package com.elys.pos.inventory.v1.validation;

import com.elys.pos.inventory.v1.entity.ItemEntity;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ItemEntityValidationTests {

//    private static Validator validator;
//
//    @BeforeAll
//    public static void setUpValidator() {
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        validator = factory.getValidator();
//    }
//
//    @Test
//    public void whenNameIsNull_thenValidationFails() {
//        ItemEntity item = ItemEntity.builder().name(null).build();
//        Set<ConstraintViolation<ItemEntity>> violations = validator.validate(item);
//        assertFalse(violations.isEmpty());
//        assertTrue(violations.stream().anyMatch(violation -> violation.getMessage().equals("Name cannot be null")));
//    }
//
//    @Test
//    public void whenNameIsBlank_thenValidationFails() {
//        ItemEntity item = ItemEntity.builder().name("").build();
//        Set<ConstraintViolation<ItemEntity>> violations = validator.validate(item);
//        assertFalse(violations.isEmpty());
//        assertTrue(violations.stream().anyMatch(violation -> violation.getMessage().equals("Name cannot be empty")));
//    }
//
//    @Test
//    public void whenCategoryIsNull_thenValidationFails() {
//        ItemEntity item = ItemEntity.builder().category(null).build();
//        Set<ConstraintViolation<ItemEntity>> violations = validator.validate(item);
//        assertFalse(violations.isEmpty());
//        assertTrue(violations.stream().anyMatch(violation -> violation.getMessage().equals("Category cannot be null")));
//    }
//
//    @Test
//    public void whenDescriptionIsNull_thenValidationFails() {
//        ItemEntity item = ItemEntity.builder().name(null).build();
//        Set<ConstraintViolation<ItemEntity>> violations = validator.validate(item);
//        assertFalse(violations.isEmpty());
//        assertTrue(violations.stream().anyMatch(violation -> violation.getMessage().equals("Description cannot be null")));
//    }
//
//    @Test
//    public void whenDescriptionIsBlank_thenValidationFails() {
//        ItemEntity item = ItemEntity.builder().name("").build();
//        Set<ConstraintViolation<ItemEntity>> violations = validator.validate(item);
//        assertFalse(violations.isEmpty());
//        assertTrue(violations.stream().anyMatch(violation -> violation.getMessage().equals("Description cannot be empty")));
//    }
//
//    @Test
//    public void whenSellingPriceIsNull_thenValidationFails() {
//        ItemEntity item = ItemEntity.builder().sellingPrice(null).build();
//        Set<ConstraintViolation<ItemEntity>> violations = validator.validate(item);
//        assertFalse(violations.isEmpty());
//        assertTrue(violations.stream().anyMatch(violation -> violation.getMessage().equals("Selling price cannot be null")));
//    }
//
//    @Test
//    public void whenSellingPriceIsNegative_thenValidationFails() {
//        ItemEntity item = ItemEntity.builder().sellingPrice(new BigDecimal("-1.00")).build();
//        Set<ConstraintViolation<ItemEntity>> violations = validator.validate(item);
//        assertFalse(violations.isEmpty());
//        assertTrue(violations.stream().anyMatch(violation -> violation.getMessage().equals("Selling price must be at least 0")));
//    }
//
//    @Test
//    public void whenSellingPriceIsMoreThanTwoDecimalPlaces_thenValidationFails() {
//        ItemEntity item = ItemEntity.builder().sellingPrice(new BigDecimal("1.111")).build();
//        Set<ConstraintViolation<ItemEntity>> violations = validator.validate(item);
//        assertFalse(violations.isEmpty());
//        assertTrue(violations.stream().anyMatch(violation -> violation.getMessage()
//                .equals("Selling price must have at most 15 total digits, with 2 decimal places")));
//    }
//
//    @Test
//    public void whenSellingPriceIsMoreThan15Digits_thenValidationFails() {
//        ItemEntity item = ItemEntity.builder().sellingPrice(new BigDecimal("12345678901234.11")).build();
//        Set<ConstraintViolation<ItemEntity>> violations = validator.validate(item);
//        assertFalse(violations.isEmpty());
//        assertTrue(violations.stream().anyMatch(violation -> violation.getMessage()
//                .equals("Selling price must have at most 15 total digits, with 2 decimal places")));
//    }
//
//    @Test
//    public void whenHsnCodeIsNull_thenValidationFails() {
//        ItemEntity item = ItemEntity.builder().hsnCode(null).build();
//        Set<ConstraintViolation<ItemEntity>> violations = validator.validate(item);
//        assertFalse(violations.isEmpty());
//        assertTrue(violations.stream().anyMatch(violation -> violation.getMessage().equals("HSN code cannot be null")));
//    }
//
//    @Test
//    public void whenHsnCodeIsBlank_thenValidationFails() {
//        ItemEntity item = ItemEntity.builder().hsnCode("").build();
//        Set<ConstraintViolation<ItemEntity>> violations = validator.validate(item);
//        assertFalse(violations.isEmpty());
//        assertTrue(violations.stream().anyMatch(violation -> violation.getMessage().equals("HSN code cannot be empty")));
//    }
//
//    @Test
//    public void whenItemTypeIsNull_thenValidationFails() {
//        ItemEntity item = ItemEntity.builder().itemType(null).build();
//        Set<ConstraintViolation<ItemEntity>> violations = validator.validate(item);
//        assertFalse(violations.isEmpty());
//        assertTrue(violations.stream().anyMatch(violation -> violation.getMessage().equals("Item type cannot be null")));
//    }
//
//    @Test
//    public void whenStockTypeIsNull_thenValidationFails() {
//        ItemEntity item = ItemEntity.builder().stockType(null).build();
//        Set<ConstraintViolation<ItemEntity>> violations = validator.validate(item);
//        assertFalse(violations.isEmpty());
//        assertTrue(violations.stream().anyMatch(violation -> violation.getMessage().equals("Stock type cannot be null")));
//    }
}
