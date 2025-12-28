package com.parejo.msvc_producto.mappers;

import com.parejo.msvc_producto.dtos.req.ProductReqDTO;
import com.parejo.msvc_producto.dtos.res.CategoryResDTO;
import com.parejo.msvc_producto.dtos.res.ProductResDTO;
import com.parejo.msvc_producto.entities.Category;
import com.parejo.msvc_producto.entities.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductMapperTest {

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private ProductMapper productMapper;

    @Test
    void toEntity_WithOnlyDtoAndCategory_ShouldMapCorrectly() {
        ProductReqDTO dto = createProductReqDTO();
        Category category = Category.builder().id(1L).name("Hardware").build();

        Product result = productMapper.toEntity(dto, category);

        assertAll("Verificación de mapeo a Entidad (sin ID)",
                () -> assertNotNull(result),
                () -> assertNull(result.getId()),
                () -> assertEquals(dto.name(), result.getName()),
                () -> assertEquals(category, result.getCategory()),
                () -> assertTrue(result.getIsActive())
        );
    }

    @Test
    void toEntity_WithIdDtoAndCategory_ShouldMapCorrectly() {
        Long id = 50L;
        ProductReqDTO dto = createProductReqDTO();
        Category category = Category.builder().id(1L).name("Hardware").build();

        Product result = productMapper.toEntity(id, dto, category);

        assertAll("Verificación de mapeo a Entidad (con ID)",
                () -> assertEquals(id, result.getId()),
                () -> assertEquals(dto.name(), result.getName()),
                () -> assertEquals(dto.price(), result.getPrice())
        );
    }

    @Test
    void toResDTO_ShouldMapEntityToResDTO() {
        Category category = Category.builder().id(1L).name("Hardware").build();
        Product entity = Product.builder()
                .id(10L)
                .name("Ryzen 5 5600H")
                .price(new BigDecimal("250.00"))
                .category(category)
                .createdAt(LocalDateTime.now())
                .build();

        CategoryResDTO categoryResDTO = new CategoryResDTO(1L, "Hardware", "HW", null, null);


        when(categoryMapper.toCategoryResDTO(category)).thenReturn(categoryResDTO);


        ProductResDTO result = productMapper.toResDTO(entity);


        assertNotNull(result);
        assertEquals(entity.getId(), result.id());
        assertEquals(categoryResDTO, result.category());
        verify(categoryMapper, times(1)).toCategoryResDTO(category);
    }


    private ProductReqDTO createProductReqDTO() {
        return new ProductReqDTO(
                "Laptop Gamer",
                "Powerful laptop",
                new BigDecimal("1200.00"),
                10,
                1L, // categoryId
                "http://image.com",
                0.00
        );
    }
}