package com.parejo.msvc_producto.mappers;

import com.parejo.msvc_producto.dtos.req.ProductReqDTO;
import com.parejo.msvc_producto.dtos.res.ProductResDTO;
import com.parejo.msvc_producto.entities.Category;
import com.parejo.msvc_producto.entities.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    final private CategoryMapper categoryMapper;

    public Product toEntity(ProductReqDTO dto, Category category) {
        return Product.builder()
                .name(dto.name())
                .description(dto.description())
                .price(dto.price())
                .stockQuantity(dto.stockQuantity())
                .category(category)
                .imageUrl(dto.imageUrl())
                .discountPercentage(dto.discountPercentage())
                .isActive(true)
                .build();
    }

    public Product toEntity(Long id, ProductReqDTO dto, Category category) {
        return Product.builder()
                .id(id)
                .name(dto.name())
                .description(dto.description())
                .price(dto.price())
                .stockQuantity(dto.stockQuantity())
                .category(category)
                .imageUrl(dto.imageUrl())
                .discountPercentage(dto.discountPercentage())
                .isActive(true)
                .build();
    }

    public ProductResDTO toResDTO(Product entity) {
        return new ProductResDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getStockQuantity(),
                categoryMapper.toCategoryResDTO(entity.getCategory()),
                entity.getImageUrl(),
                entity.getDiscountPercentage()
        );
    }

}
