package com.elys.pos.inventory.v1.specification;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public static <T> Specification<T> uuidFieldEquals(String field, UUID value) {
        return (root, query, criteriaBuilder) -> (value != null)
                ? criteriaBuilder.equal(root.get(field), value)
                : criteriaBuilder.conjunction();
    }

    public static <T> Specification<T> dateFieldBetween(String field, LocalDate dateLowerBound, LocalDate dateUpperBound) {
        return (root, query, criteriaBuilder) -> {
            if (dateLowerBound != null && dateUpperBound != null) {
                LocalDateTime dateTimeLowerBound = dateLowerBound.atStartOfDay();
                LocalDateTime dateTimeUpperBound = dateUpperBound.plusDays(1).atStartOfDay().minusNanos(1);
                return criteriaBuilder.between(root.get(field), dateTimeLowerBound, dateTimeUpperBound);
            } else {
                return criteriaBuilder.conjunction();
            }
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

    public static <T> Specification<T> numberFieldBetween(String field, Number numberLowerBound, Number numberUpperBound) {
        BigDecimal num1 = toBigDecimal(numberLowerBound);
        BigDecimal num2 = toBigDecimal(numberUpperBound);
        return (root, query, criteriaBuilder) -> (num1 != null && num2 != null)
                ? criteriaBuilder.between(root.get(field), num1, num2)
                : criteriaBuilder.conjunction();
    }
}
