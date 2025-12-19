package com.parejo.msvc_producto.mappers;

import com.parejo.msvc_producto.dtos.req.ProductReqDTO;
import com.parejo.msvc_producto.dtos.res.ProductResDTO;
import com.parejo.msvc_producto.entities.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductReqDTO dto) {
        return Product.builder()
                .name(dto.name())
                .description(dto.description())
                .price(dto.price())
                .stockQuantity(dto.stockQuantity())
                .category(dto.category())
                .imageUrl(dto.imageUrl())
                .discountPercentage(dto.discountPercentage())
                .build();
    }

    public ProductResDTO toResDTO(Product entity) {
        return new ProductResDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getStockQuantity(),
                entity.getCategory(),
                entity.getImageUrl(),
                entity.getDiscountPercentage()
        );
    }

}
