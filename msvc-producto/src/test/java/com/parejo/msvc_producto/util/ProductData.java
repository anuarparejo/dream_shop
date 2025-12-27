package com.parejo.msvc_producto.util;

import com.parejo.msvc_producto.dtos.req.CategoryReqDTO;
import com.parejo.msvc_producto.dtos.req.ProductReqDTO;
import com.parejo.msvc_producto.dtos.res.CategoryResDTO;
import com.parejo.msvc_producto.dtos.res.ProductResDTO;
import com.parejo.msvc_producto.entities.Category;
import com.parejo.msvc_producto.entities.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductData {

    public static Category createCategory() {
        List<Product> products = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        return Category.builder()
                .id(1L)
                .name("Tecnologia")
                .type("Moderna")
                .products(products)
                .isActive(true)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }


    public static Product createProduct() {
        LocalDateTime now = LocalDateTime.now();


        return Product.builder()
                .id(1L)
                .name("Laptop")
                .description("Ryzen 5 3045, Ryzen 5 3045, Ryzen 5 3045, Ryzen 5 3045")
                .price(new BigDecimal("3.00"))
                .stockQuantity(30)
                .category(createCategory())
                .imageUrl("https://www.google.com")
                .discountPercentage(0.00)
                .isActive(true)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public static CategoryResDTO createCategoryResDTO() {
        return new CategoryResDTO(
                1L,
                "Tecnologia",
                "Moderna",
                LocalDateTime.now(),
                LocalDateTime.now());
    }

    public static ProductResDTO createProductResDTO() {
        return new ProductResDTO(
                1L,
                "Laptop",
                "Ryzen 5 3045, Ryzen 5 3045, Ryzen 5 3045, Ryzen 5 3045",
                new BigDecimal("3.00"),
                30,
                createCategoryResDTO(),
                "https://www.google.com",
                0.00,
                LocalDateTime.now(),
                LocalDateTime.now());
    }

    public static CategoryReqDTO  createCategoryReqDTO() {
        return new CategoryReqDTO(
                "Tecnologia",
                "Moderna");
    }

    public static ProductReqDTO createProductReqDTO() {
        return new ProductReqDTO("Laptop",
                "Ryzen 5 3045, Ryzen 5 3045, Ryzen 5 3045, Ryzen 5 3045",
                new BigDecimal("3.00"),
                30,
                1L,
                "https://www.google.com",
                0.00);
    }
}