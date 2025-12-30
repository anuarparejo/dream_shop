package com.parejo.msvc_producto.dtos.req;

import java.math.BigDecimal;

public record ProductSearchDTO(
        String name,
        Long categoryId,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Boolean onlyWithStock
) {}