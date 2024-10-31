package com.elys.pos.inventory.repository.v1;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class SpecificationUtils {

    public static <T> Specification<T> stringFieldContains(String field, String value) {
        return stringFieldContains(field, value, false);
    }

    public static <T> Specification<T> stringFieldContains(String field, String value, boolean compareCase) {
        if (compareCase) {
            return (root, query, criteriaBuilder) -> (value != null && !value.isEmpty())
                    ? criteriaBuilder.like(root.get(field), "%" + value + "%")
                    : criteriaBuilder.conjunction();
        } else {
            return (root, query, criteriaBuilder) -> (value != null && !value.isEmpty())
                    ? criteriaBuilder.like(criteriaBuilder.lower(root.get(field)), "%" + value.toLowerCase() + "%")
                    : criteriaBuilder.conjunction();
        }
    }

    public static <T> Specification<T> uuidFieldEquals(String field, String value) {
        return (root, query, criteriaBuilder) -> (value != null && !value.isEmpty() && isValidUUID(value))
                ? criteriaBuilder.equal(root.get(field), UUID.fromString(value))
                : criteriaBuilder.conjunction();
    }

    private static boolean isValidUUID(String value) {
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static <T> Specification<T> dateFieldEquals(String field, LocalDate date) {
        return (root, query, criteriaBuilder) -> date != null
                ? criteriaBuilder.equal(root.get(field), date)
                : criteriaBuilder.conjunction();
    }

    public static <T> Specification<T> dateFieldLessThan(String field, LocalDate date) {
        return (root, query, criteriaBuilder) -> date != null
                ? criteriaBuilder.lessThan(root.get(field), date)
                : criteriaBuilder.conjunction();
    }

    public static <T> Specification<T> dateFieldGreaterThan(String field, LocalDate date) {
        return (root, query, criteriaBuilder) -> date != null
                ? criteriaBuilder.greaterThan(root.get(field), date)
                : criteriaBuilder.conjunction();
    }

    public static <T> Specification<T> dateFieldBetween(String field, LocalDate lowerDate, LocalDate higherDate) {
        return (root, query, criteriaBuilder) -> {
            if (lowerDate != null && higherDate != null) {
                return criteriaBuilder.between(root.get(field), lowerDate, higherDate);
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static <T> Specification<T> booleanFieldEquals(String field, boolean flag) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(field), flag);
    }

    private static BigDecimal toBigDecimal(Number number) {
        return number != null ? new BigDecimal(number.toString()) : null;
    }

    public static <T> Specification<T> numberFieldEqualTo(String field, Number number) {
        BigDecimal value = toBigDecimal(number);
        return ((root, query, criteriaBuilder) -> (value != null)
                ? criteriaBuilder.equal(root.get(field), value)
                : criteriaBuilder.conjunction()
        );
    }

    public static <T> Specification<T> numberFieldGreaterThan(String field, Number number) {
        BigDecimal value = toBigDecimal(number);
        return (root, query, criteriaBuilder) -> (value != null)
                ? criteriaBuilder.greaterThan(root.get(field), value)
                : criteriaBuilder.conjunction();
    }

    public static <T> Specification<T> numberFieldLessThan(String field, Number number) {
        BigDecimal value = toBigDecimal(number);
        return (root, query, criteriaBuilder) -> (value != null)
                ? criteriaBuilder.lessThan(root.get(field), value)
                : criteriaBuilder.conjunction();
    }

    public static <T> Specification<T> numberFieldBetween(String field, Number lowerNumber, Number higherNumber) {
        BigDecimal value1 = toBigDecimal(lowerNumber);
        BigDecimal value2 = toBigDecimal(higherNumber);
        return (root, query, criteriaBuilder) -> (value1 != null && value2 != null)
                ? criteriaBuilder.between(root.get(field), value1, value2)
                : criteriaBuilder.conjunction();
    }
}
