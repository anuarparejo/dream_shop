package com.parejo.msvc_producto.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
@EnableFeignClients
public class JpaConfig {
    // Clase para evitar errores en el test de los controller
    // Esta clase solo se cargará en el contexto real o en tests de JPA,
    // pero @WebMvcTest la ignorará, evitando el error.
}
