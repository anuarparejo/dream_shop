package com.parejo.msvc_producto.dtos.res;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResDTO(
        Long
        id,

        String
        name,

        String
        description,

        BigDecimal
        price,

        Integer
        stockQuantity,

        CategoryResDTO
        category,

        String
        imageUrl,

        Double
        discountPercentage,

        LocalDateTime
        createdAt,

        LocalDateTime
        updatedAt
) {
}
