package com.parejo.msvc_producto.mappers;

import com.parejo.msvc_producto.dtos.req.CategoryReqDTO;
import com.parejo.msvc_producto.dtos.res.CategoryResDTO;
import com.parejo.msvc_producto.entities.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toCategory(CategoryReqDTO dto) {
       return Category.builder()
                .name(dto.name())
                .type(dto.type())
                .isActive(true)
                .build();
    }

    public CategoryResDTO toCategoryResDTO(Category category) {
        return new CategoryResDTO(
                category.getId(),
                category.getName(),
                category.getType(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}
