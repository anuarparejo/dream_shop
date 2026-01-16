package com.parejo.msvc_producto.repositories;

import com.parejo.msvc_producto.entities.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest

class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Debe encontrar productos por nombre (IgnoreCase) y que estén activos")
    void findByNameContainingIgnoreCaseAndIsActiveTrue_ShouldWork() {
        // GIVEN: Guardamos datos reales en la DB de prueba
        Product p1 = new Product();
        p1.setName("Laptop Gamer");
        p1.setIsActive(true);
        p1.setPrice(new BigDecimal("1500.0"));

        Product p2 = new Product();
        p2.setName("laptop de oficina"); // Minúsculas
        p2.setIsActive(true);
        p2.setPrice(new BigDecimal("800.0"));

        Product p3 = new Product();
        p3.setName("Laptop vieja");
        p3.setIsActive(false); // Inactivo (no debe salir)
        p3.setPrice(new BigDecimal("200.0"));

        productRepository.saveAll(List.of(p1, p2, p3));

        // WHEN
        Page<Product> result = productRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(
                "LAPTOP", PageRequest.of(0, 10));

        // THEN
        assertNotNull(result);
        assertEquals(2, result.getTotalElements(), "Debe encontrar 2 laptops activas ignorando casing");
        assertTrue(result.getContent().stream().allMatch(Product::getIsActive));

    }
}
