package com.parejo.msvc_producto.services;

import com.parejo.msvc_producto.dtos.req.CategoryReqDTO;
import com.parejo.msvc_producto.dtos.res.CategoryResDTO;
import com.parejo.msvc_producto.entities.Category;
import com.parejo.msvc_producto.entities.Product;
import com.parejo.msvc_producto.exceptions.ResourceNotFoundException;
import com.parejo.msvc_producto.mappers.CategoryMapper;
import com.parejo.msvc_producto.repositories.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.parejo.msvc_producto.util.ProductData.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void findAll_ShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Category category = createCategory();
        CategoryResDTO categoryResDTO = createCategoryResDTO();
        Page<Category> page = new PageImpl<>(List.of(category));

        when(categoryRepository.findAllByIsActiveTrue(pageable)).thenReturn(page);
        when(categoryMapper.toCategoryResDTO(any())).thenReturn(categoryResDTO);

        Page<CategoryResDTO> result = categoryService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(categoryRepository).findAllByIsActiveTrue(pageable);
    }

    @Test
    void deleteById_ShouldThrowException_WhenHasProducts() {
        Category category = createCategory();
        Product product = createProduct();
        category.setProducts(List.of(product));

        when(categoryRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(category));

        assertThrows(DataIntegrityViolationException.class, () -> categoryService.deleteById(1L));
        assertTrue(category.getIsActive());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void deleteById_ShouldDeactivate_WhenNoProducts() {
        Category category = createCategory();
        category.setIsActive(true);
        category.setProducts(Collections.emptyList());

        when(categoryRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(category));

        categoryService.deleteById(1L);

        assertFalse(category.getIsActive());
        verify(categoryRepository).findByIdAndIsActiveTrue(1L);
    }

    @Test
    void save_ShouldReturnSavedDTO() {
        CategoryReqDTO reqDto = createCategoryReqDTO();
        Category category = createCategory();
        category.setId(null);
        Category savedCategory = createCategory();
        CategoryResDTO categoryResDTO = createCategoryResDTO();

        when(categoryMapper.toCategory(reqDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(savedCategory);
        when(categoryMapper.toCategoryResDTO(savedCategory)).thenReturn(categoryResDTO);

        CategoryResDTO result = categoryService.save(reqDto);

        assertEquals(1L, result.id());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void findById_ShouldReturnDTO_WhenIdExists() {
        Category category = createCategory();
        CategoryResDTO categoryResDTO = createCategoryResDTO();

        when(categoryRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toCategoryResDTO(category)).thenReturn(categoryResDTO);

        CategoryResDTO result = categoryService.findById(1L);

        assertNotNull(result);
        assertThat(result).isNotNull().usingRecursiveComparison().isEqualTo(categoryResDTO);
    }

    @Test
    void findById_ShouldThrowException_WhenIdDoesNotExist() {
        when(categoryRepository.findByIdAndIsActiveTrue(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.findById(99L));
    }

    @Test
    void update_ShouldReturnUpdatedProductResDTO_WhenProductAndCategoryExist() {
        Long id = 1L;
        CategoryReqDTO updateDto = createCategoryReqDTO();

        Category existingCategory = createCategory();
        existingCategory.setId(id);

        Category updatedCategory = createCategory();
        updatedCategory.setId(id);
        updatedCategory.setName("Tecnologia");

        CategoryResDTO expectedRes = createCategoryResDTO();


        when(categoryRepository.findByIdAndIsActiveTrue(id)).thenReturn(Optional.of(existingCategory));
        when(categoryMapper.toCategory(id, updateDto)).thenReturn(updatedCategory);
        when(categoryRepository.save(updatedCategory)).thenReturn(updatedCategory);
        when(categoryMapper.toCategoryResDTO(updatedCategory)).thenReturn(expectedRes);


        CategoryResDTO result = categoryService.update(id, updateDto);

        assertNotNull(result);
        assertEquals("Tecnologia", result.name());
        assertEquals(id, result.id());

        verify(categoryRepository).findByIdAndIsActiveTrue(id);
        verify(categoryMapper).toCategory(eq(id), eq(updateDto));
        verify(categoryRepository).save(updatedCategory);
    }

    @Test
    void update_ShouldThrowException_WhenCategoryDoesNotExist() {
        Long id = 99L;
        CategoryReqDTO updateDto = createCategoryReqDTO();

        when(categoryRepository.findByIdAndIsActiveTrue(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                categoryService.update(id, updateDto)
        );

        verify(categoryRepository, never()).save(any());
        verify(categoryMapper, never()).toCategory(anyLong(), any());
    }
}
