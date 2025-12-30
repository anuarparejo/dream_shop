package com.parejo.msvc_producto.repositories;

import com.parejo.msvc_producto.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Page<Product> findAllByIsActiveTrue(Pageable pageable);
    Optional<Product> findByIdAndIsActiveTrue(Long id);
    Page<Product> findByNameContainingIgnoreCaseAndIsActiveTrue(String name, Pageable pageable);

}
