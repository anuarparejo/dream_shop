package com.parejo.msvc_producto.dtos.req;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;


public record ProductReqDTO(
        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        String name,

        @NotBlank(message = "La descripcion no puede estar vacía")
        @Size(min = 20, max = 100, message = "La descripcion debe tener entre 20 y 100 caracteres")
        String description,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
        BigDecimal price,

        @Min(value = 0, message = "El stock no puede ser negativo")
        @NotNull(message = "El stock es obligatorio")
        Integer stockQuantity,

        @Min(value = 0, message = "La categoria no puede ser negativa")
        @NotNull(message = "La categoria es obligatoria")
        Long categoryId,

        @URL(message = "El campo url debe tener un formato valido")
        @NotBlank(message = "La URL no puede estar vacía")
        String imageUrl,

        @DecimalMin(value = "0.0", message = "El descuento no puede ser negativo")
         Double discountPercentage
) {
}
