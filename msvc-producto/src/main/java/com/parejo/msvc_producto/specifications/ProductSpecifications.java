package com.parejo.msvc_producto.specifications;

import com.parejo.msvc_producto.entities.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecifications {

    public static Specification<Product> isNotDeleted() {
        return (root, query, cb) -> cb.equal(root.get("isActive"), true);
    }

    public static Specification<Product> hasName(String name) {
        return (root, query, cb) -> name == null ? null :
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Product> hasCategoryId(Long categoryId) {
        return (root, query, cb) -> categoryId == null ? null :
                cb.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Product> priceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, cb) -> {
            if (minPrice == null && maxPrice == null) return null;
            if (minPrice != null && maxPrice != null) return cb.between(root.get("price"), minPrice, maxPrice);
            if (minPrice != null) return cb.greaterThanOrEqualTo(root.get("price"), minPrice);
            return cb.lessThanOrEqualTo(root.get("price"), maxPrice);
        };
    }

    public static Specification<Product> hasStock() {
        return (root, query, cb) -> cb.greaterThan(root.get("stockQuantity"), 0);
    }
}