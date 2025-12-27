package com.parejo.msvc_producto.services;


import com.parejo.msvc_producto.dtos.req.CategoryReqDTO;
import com.parejo.msvc_producto.dtos.res.CategoryResDTO;
import com.parejo.msvc_producto.entities.Category;
import com.parejo.msvc_producto.exceptions.ResourceNotFoundException;
import com.parejo.msvc_producto.mappers.CategoryMapper;
import com.parejo.msvc_producto.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    final private CategoryRepository categoryRepository;
    final private CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResDTO> findAll(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAllByIsActiveTrue(pageable);
        return categories.map(categoryMapper::toCategoryResDTO);
    }

    @Override
    @Transactional
    public CategoryResDTO save(CategoryReqDTO dto) {
        Category category = categoryMapper.toCategory(dto);
        Category categorySaved = categoryRepository.save(category);

        log.info("Categoria creada exitosamente con ID: {} y nombre: {}", category.getId(), category.getName());
        return categoryMapper.toCategoryResDTO(categorySaved);
    }

    @Override
    @Transactional
    public CategoryResDTO update(Long id, CategoryReqDTO dto) {
        findCategoryOrThrow(id);
        Category category = categoryMapper.toCategory(id, dto);
        Category categorySaved = categoryRepository.save(category);

        log.info("Categoria actualizada exitosamente con ID: {} y nombre: {}", category.getId(), category.getName());
        return categoryMapper.toCategoryResDTO(categorySaved);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResDTO findById(Long id) {
        Category category = findCategoryOrThrow(id);
        return categoryMapper.toCategoryResDTO(category);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Category category = findCategoryOrThrow(id);
        if (!category.getProducts().isEmpty()) {
            throw new DataIntegrityViolationException("La categoria tiene productos enlazados");
        }
        category.setIsActive(false);
        log.info("Categoria desactivada exitosamente con ID: {} y nombre: {}", category.getId(), category.getName());
    }

    private Category findCategoryOrThrow(Long id) {
        return categoryRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoria no encontrada"));
    }
}
