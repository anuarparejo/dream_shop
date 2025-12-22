package com.parejo.msvc_producto.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class Product extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    private String description;

    @Positive(message = "El precio debe ser mayor a cero")
    private BigDecimal price;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stockQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private String imageUrl;

    // Descuento porcentual
    private Double discountPercentage;

    private Boolean isActive;
}
