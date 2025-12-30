package com.parejo.msvc_producto.services;

import com.parejo.msvc_producto.dtos.req.ProductReqDTO;
import com.parejo.msvc_producto.dtos.req.ProductSearchDTO;
import com.parejo.msvc_producto.dtos.res.ProductResDTO;
import com.parejo.msvc_producto.entities.Category;
import com.parejo.msvc_producto.entities.Product;
import com.parejo.msvc_producto.exceptions.ResourceNotFoundException;
import com.parejo.msvc_producto.mappers.ProductMapper;
import com.parejo.msvc_producto.repositories.CategoryRepository;
import com.parejo.msvc_producto.repositories.ProductRepository;
import com.parejo.msvc_producto.util.ProductData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.parejo.msvc_producto.util.ProductData.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductMapper productMapper;
    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void save_ShouldReturnProductResDTO_WhenCategoryExists() {
        Long categoryId = 1L;
        ProductReqDTO req = ProductData.createProductReqDTO();
        Category category = ProductData.createCategory();
        category.setId(categoryId);
        Product product = createProduct();
        ProductResDTO expectedRes = createProductResDTO();

        when(categoryRepository.findByIdAndIsActiveTrue(category.getId())).thenReturn(Optional.of(category));
        when(productMapper.toEntity(req, category)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toResDTO(product)).thenReturn(expectedRes);

        ProductResDTO result = productService.save(req);

        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison() // <--Compara campo por campo recursivamente
                .isEqualTo(expectedRes);

        verify(productRepository, times(1)).save(product);
        verify(categoryRepository).findByIdAndIsActiveTrue(categoryId);
    }

    @Test
    void findByFilters_ShouldReturnPageOfDtos() {
        // GIVEN
        ProductSearchDTO filters = new ProductSearchDTO("Laptop", 1L, BigDecimal.ZERO, BigDecimal.valueOf(1000), true);
        Pageable pageable = PageRequest.of(0, 10);
        Product product = new Product();
        Page<Product> productPage = new PageImpl<>(List.of(product));

        when(productRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(productPage);
        when(productMapper.toResDTO(any(Product.class))).thenReturn(mock(ProductResDTO.class));

        // WHEN
        Page<ProductResDTO> result = productService.findByFilters(filters, pageable);

        // THEN
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(productRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void save_ShouldThrowException_WhenCategoryDoesNotExist() {


        ProductReqDTO req = ProductData.createProductReqDTO();
        when(categoryRepository.findByIdAndIsActiveTrue(req.categoryId())).thenReturn(Optional.empty());


        assertThrows(ResourceNotFoundException.class, () -> productService.save(req));

        verify(categoryRepository, times(1)).findByIdAndIsActiveTrue(req.categoryId());
        verify(productRepository, never()).save(any(Product.class));
        verifyNoInteractions(productMapper);
    }

    @Test
    void getById_ShouldReturnProductResDTO_WhenProductExists() {
        Product productSaved = createProduct();
        ProductResDTO expectedRes = createProductResDTO();

        when(productRepository.findByIdAndIsActiveTrue(productSaved.getId())).thenReturn(Optional.of(productSaved));
        when(productMapper.toResDTO(productSaved)).thenReturn(expectedRes);

        ProductResDTO result = productService.findById(productSaved.getId());

        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedRes);

        verify(productRepository, times(1)).findByIdAndIsActiveTrue(productSaved.getId());
    }

    @Test
    void getById_ShouldThrowException_WhenProductDoesNotExist() {
        Long idNotExist = -1L;

        when(productRepository.findByIdAndIsActiveTrue(idNotExist)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.findById(idNotExist));
        verify(productRepository, times(1)).findByIdAndIsActiveTrue(idNotExist);
        verify(productMapper, never()).toResDTO(any());
    }

    @Test
    void deleteById_ShouldSetIsActiveToFalse_WhenProductExists() {

        Long productId = 1L;
        Product productInDb = createProduct();
        productInDb.setIsActive(true);

        when(productRepository.findByIdAndIsActiveTrue(productId))
                .thenReturn(Optional.of(productInDb));

        productService.deleteById(productId);

        assertFalse(productInDb.getIsActive(), "El producto debería estar desactivado");

        verify(productRepository, times(1)).findByIdAndIsActiveTrue(productId);
    }

    @Test
    void deleteById_ShouldThrowException_WhenProductDoesNotExist() {
        Long idNotExist = 1L;
        when(productRepository.findByIdAndIsActiveTrue(idNotExist)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.deleteById(idNotExist));
        verify(productRepository, times(1)).findByIdAndIsActiveTrue(idNotExist);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void findAll_ShouldReturnProductResDTO_WhenProductExists() {
        Product product = createProduct();
        ProductResDTO expectedRes = createProductResDTO();
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(product), pageable, 1);

        when(productRepository.findAllByIsActiveTrue(pageable)).thenReturn(productPage);
        when(productMapper.toResDTO(product)).thenReturn(expectedRes);

        Page<ProductResDTO> result = productService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertThat(result.getContent().getFirst())
                .usingRecursiveComparison()
                .isEqualTo(expectedRes);

        verify(productRepository, times(1)).findAllByIsActiveTrue(pageable);
        verify(productMapper, times(1)).toResDTO(product);
    }

    @Test
    void update_ShouldReturnUpdatedProductResDTO_WhenProductAndCategoryExist() {
        Long productId = 1L;
        Long newCategoryId = 1L;
        ProductReqDTO updateDto = createProductReqDTO();

        Category newCategory = createCategory();
        Product existingProduct = createProduct();
        Product updatedProduct = createProduct();
        updatedProduct.setName("Laptop Pro");

        ProductResDTO expectedRes = createProductResDTO();

        when(productRepository.findByIdAndIsActiveTrue(productId)).thenReturn(Optional.of(existingProduct));

        when(categoryRepository.findByIdAndIsActiveTrue(newCategoryId)).thenReturn(Optional.of(newCategory));
        when(productMapper.toEntity(productId, updateDto, newCategory)).thenReturn(updatedProduct);
        when(productRepository.save(updatedProduct)).thenReturn(updatedProduct);
        when(productMapper.toResDTO(updatedProduct)).thenReturn(expectedRes);

        ProductResDTO result = productService.update(productId, updateDto);

        assertNotNull(result);
        verify(productRepository).findByIdAndIsActiveTrue(productId);
        verify(categoryRepository).findByIdAndIsActiveTrue(newCategoryId);
        verify(productRepository).save(updatedProduct);
    }

    @Test
    void updateShouldThrowException_WhenProductDoesNotExist() {
        Long idNotExist = 1L;
        ProductReqDTO productReqDTO = createProductReqDTO();

        when(productRepository.findByIdAndIsActiveTrue(idNotExist)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.update(idNotExist, productReqDTO));

        verify(productRepository, times(1)).findByIdAndIsActiveTrue(idNotExist);
        verify(productRepository, never()).save(any(Product.class));
    }










}