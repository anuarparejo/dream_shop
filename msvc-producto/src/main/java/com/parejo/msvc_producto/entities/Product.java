package com.parejo.msvc_producto.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@SuperBuilder
public class Product extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer stockQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private String imageUrl;

    // Descuento porcentual
    private Double discountPercentage;

    private Boolean isActive;
}
