package com.parejo.msvc_producto.services;

import com.parejo.msvc_producto.dtos.req.ProductReqDTO;
import com.parejo.msvc_producto.dtos.res.ProductResDTO;
import com.parejo.msvc_producto.entities.Category;
import com.parejo.msvc_producto.entities.Product;
import com.parejo.msvc_producto.exceptions.ResourceNotFoundException;
import com.parejo.msvc_producto.mappers.ProductMapper;
import com.parejo.msvc_producto.repositories.CategoryRepository;
import com.parejo.msvc_producto.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResDTO> findAll(Pageable pageable) {
        Page<Product> products = productRepository.findAllByIsActiveTrue(pageable);
        return products.map(productMapper::toResDTO);
    }

    @Override
    @Transactional
    public ProductResDTO save(@NonNull ProductReqDTO dto) {
        Category category = findCategoryOrThrow(dto.categoryId());
        Product product = productMapper.toEntity(dto, category);
        Product productSaved = productRepository.save(product);

        return productMapper.toResDTO(productSaved);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResDTO findById(Long id) {
        Product product = findProductOrThrow(id);
        return productMapper.toResDTO(product);
    }

    @Override
    public Page<ProductResDTO> findByCategoryIdAndIsActiveTrue(Long categoryId, Pageable pageable) {
        Category category = findCategoryOrThrow(categoryId);

        Page<Product> products = productRepository.findByCategoryIdAndIsActiveTrue(category.getId(), pageable);

        return products.map(productMapper::toResDTO);
    }


    @Override
    @Transactional
    public void deleteById(Long id) {
        Product product = findProductOrThrow(id);
        product.setIsActive(false);
    }

    @Override
    @Transactional
    public ProductResDTO update(Long id, ProductReqDTO dto) {
        findProductOrThrow(id);
        Category category = findCategoryOrThrow(dto.categoryId());

        Product productSaved = productMapper.toEntity(id, dto, category);

        return productMapper.toResDTO(productRepository.save(productSaved));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResDTO> findByName(String name, Pageable pageable) {
        Page<Product> products = productRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name, pageable);
        return products.map(productMapper::toResDTO);
    }

    private Product findProductOrThrow(Long id) {
        return productRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Producto no encontrado"));
    }

    private Category findCategoryOrThrow(Long CategoryId) {
        return categoryRepository.findByIdAndIsActiveTrue(CategoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoria no encontrada"));
    }


}