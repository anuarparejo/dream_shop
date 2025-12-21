package com.parejo.msvc_producto.repositories;

import com.parejo.msvc_producto.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // metodo para buscar por categoría
    Page<Product> findByCategoryIdAndIsActiveTrue(Long id, Pageable pageable);
    Page<Product> findAllByIsActiveTrue(Pageable pageable);
    Optional<Product> findByIdAndIsActiveTrue(Long id);
}
