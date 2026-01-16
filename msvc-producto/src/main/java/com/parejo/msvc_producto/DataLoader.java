package com.parejo.msvc_producto;

import com.parejo.msvc_producto.entities.Category;
import com.parejo.msvc_producto.entities.Product;
import com.parejo.msvc_producto.repositories.CategoryRepository;
import com.parejo.msvc_producto.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        // Solo insertamos si la base de datos está vacía para evitar duplicados
        if (categoryRepository.count() == 0) {

            // 1. Crear Categorías
            Category tech = Category.builder().name("Tecnología").type("ELECTRONICS").isActive(true).build();
            Category home = Category.builder().name("Hogar").type("HOME_DECOR").isActive(true).build();
            categoryRepository.saveAll(List.of(tech, home));

            // 2. Crear Productos
            Product p1 = Product.builder()
                    .name("Laptop Gamer Ryzen 5")
                    .description("Potente laptop con procesador Ryzen 5 5600H y 16GB RAM.")
                    .price(new BigDecimal("899.99"))
                    .stockQuantity(15)
                    .category(tech)
                    .imageUrl("https://images.unsplash.com/photo-1593642702821-c8da6771f0c6")
                    .discountPercentage(10.0)
                    .isActive(true)
                    .build();

            Product p2 = Product.builder()
                    .name("Monitor 4K UltraWide")
                    .description("Monitor de 34 pulgadas con resolución 4K y 144Hz de tasa de refresco.")
                    .price(new BigDecimal("450.00"))
                    .stockQuantity(8)
                    .category(tech)
                    .imageUrl("https://images.unsplash.com/photo-1527443224154-c4a3942d3acf")
                    .discountPercentage(5.0)
                    .isActive(true)
                    .build();

            Product p3 = Product.builder()
                    .name("Silla Ergonómica Pro")
                    .description("Silla ajustable con soporte lumbar para largas jornadas de trabajo.")
                    .price(new BigDecimal("199.50"))
                    .stockQuantity(25)
                    .category(home)
                    .imageUrl("https://images.unsplash.com/photo-1505843490538-5133c6c7d0e1")
                    .discountPercentage(0.0)
                    .isActive(true)
                    .build();

            productRepository.saveAll(List.of(p1, p2, p3));

            System.out.println("--- DATOS DE PRUEBA CARGADOS CORRECTAMENTE ---");
        }
    }
}