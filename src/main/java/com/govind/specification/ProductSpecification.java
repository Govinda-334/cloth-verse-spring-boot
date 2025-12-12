package com.govind.specification;

import org.springframework.data.jpa.domain.Specification;
import com.govind.models.Product;
import jakarta.persistence.criteria.Join;

public class ProductSpecification {

    public static Specification<Product> nameContains(String name) {
        return (root, query, cb) ->
                (name == null || name.isBlank())
                        ? null
                        : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Product> hasCategory(Long categoryId) {
        return (root, query, cb) -> {
            if (categoryId == null) return null;

            Join<Object, Object> join = root.join("category");
            return cb.equal(join.get("id"), categoryId);
        };
    }

    public static Specification<Product> priceBetween(Double min, Double max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return null;

            if (min != null && max != null) {
                return cb.between(root.get("price"), min, max);
            } else if (min != null) {
                return cb.greaterThanOrEqualTo(root.get("price"), min);
            } else {
                return cb.lessThanOrEqualTo(root.get("price"), max);
            }
        };
    }
    public static Specification<Product> hasBrandName(String brandName) {
        return (root, query, cb) -> {
            if (brandName == null || brandName.isBlank()) {
                return cb.conjunction();
            }

            return cb.like(
                    cb.lower(root.get("brandName")),
                    "%" + brandName.toLowerCase() + "%"
            );
        };
    }




    public static Specification<Product> stockBetween(Integer min, Integer max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return null;

            if (min != null && max != null) {
                return cb.between(root.get("stock"), min, max);
            } else if (min != null) {
                return cb.greaterThanOrEqualTo(root.get("stock"), min);
            } else {
                return cb.lessThanOrEqualTo(root.get("stock"), max);
            }
        };
    }

    public static Specification<Product> inStock(Boolean inStock) {
        return (root, query, cb) -> {
            if (inStock == null) return null;

            return inStock
                    ? cb.greaterThan(root.get("stock"), 0)
                    : cb.equal(root.get("stock"), 0);
        };
    }

    public static Specification<Product> isActive(Boolean active) {
        return (root, query, cb) ->
                (active == null) ? null : cb.equal(root.get("active"), active);
    }
}