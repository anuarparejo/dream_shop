package com.parejo.msvc_producto.repositories;

import com.parejo.msvc_producto.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Long> {
   Optional<Category> findByIdAndIsActiveTrue(Long id);
    Page<Category> findAllByIsActiveTrue(Pageable pageable);

}
