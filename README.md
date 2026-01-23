🛒 DreamShop – Backend E-Commerce (Java / Spring Boot)

DreamShop es un proyecto backend de e-commerce desarrollado en Java 21 y Spring Boot, diseñado como una simulación realista de un sistema empresarial, con enfoque en buenas prácticas de backend, seguridad y arquitectura de microservicios.

El proyecto fue creado con fines formativos y profesionales, orientado a demostrar criterio técnico y comprensión de sistemas distribuidos, más allá de la interfaz de usuario.

## 🚀 Tecnologías Principales

* **Java 21** (Optimizado para alto rendimiento)
* **Spring Boot 3.4.1**
* **Spring Data JPA**
* **Bases de Datos Políglotas:** * **PostgreSQL 16** (Microservicio de Productos)
    * **MySQL 8.0** (Microservicio de Usuarios)
* **Docker & Docker Compose** (Orquestación de infraestructura)
* **Lombok & MapStruct** (Código limpio y mapeo eficiente)

🎯 Objetivo del proyecto

Simular un backend de e-commerce que:

Separe responsabilidades por dominio

Aplique autenticación y autorización realistas

Permita comunicación desacoplada entre servicios

Utilice contenedores para facilitar la ejecución del entorno

---

## 🏗️ Arquitectura del Sistema

El proyecto se divide en módulos independientes que se comunican de forma desacoplada, cada uno con su propio ciclo de vida y almacenamiento:

🔹 Microservicio de Usuarios (msvc-usuarios)

Responsable de:

Registro y autenticación de usuarios

Generación y validación de JWT

Gestión de roles y autorización por endpoint

Base de datos: MySQL
Elegida por su simplicidad y uso común en sistemas de autenticación.


🔹 Microservicio de Productos (msvc-productos)

Responsable de:

Gestión del catálogo de productos

Categorías, precios y stock

Exposición de endpoints protegidos según rol

Base de datos: PostgreSQL
Elegida por su robustez e integridad de datos.


🔐 Seguridad

El sistema implementa Spring Security con autenticación basada en JWT, siguiendo un enfoque stateless:

Flujo de login que genera un token JWT

Validación del token en cada request mediante un filtro personalizado

Autorización por roles (ROLE_USER, ROLE_ADMIN)

Restricción de operaciones de escritura (POST, PUT, DELETE) al rol ADMIN

Las operaciones de lectura (GET) son públicas, permitiendo el acceso al catálogo sin autenticación.

---

🔄 Comunicación entre Microservicios

Los microservicios se comunican mediante OpenFeign, lo que permite:

Consumo desacoplado de endpoints

Uso de DTOs para evitar dependencias directas entre dominios

Centralización del consumo HTTP

Actualmente se utilizan URLs directas para simplificar el entorno local. En un escenario productivo, este enfoque podría evolucionar hacia service discovery o un API Gateway.

⚠️ Manejo de Errores

Cada microservicio cuenta con un manejo centralizado de excepciones utilizando @RestControllerAdvice, permitiendo:

Respuestas HTTP consistentes

Manejo de errores de validación, autenticación y recursos no encontrados

Estructura uniforme de errores para consumo por clientes externos

El manejo avanzado de fallos distribuidos se encuentra fuera del alcance actual del proyecto.

## 🛠️ Configuración y Despliegue con Docker

Gracias a Docker, no necesitas instalar las bases de datos localmente. Todo el entorno se levanta con un solo comando.

### Requisitos previos
* Docker Desktop instalado.
* Maven 3.9+ (o usar el wrapper `./mvnw`).

### Pasos para ejecutar:

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/anuarparejo/dream_shop.git
   cd DreamShop

2. **Compilar los microservicios:**
    ```bash
        ./mvnw clean package -DskipTests

3. **Levantar la infraestructura:**
    ```bash
       docker-compose up --build
   
📊 Endpoints de Monitoreo (Actuator)
Cada microservicio incluye Spring Boot Actuator para verificar su estado de salud:

Health Check Productos: http://localhost:8081/actuator/health

Health Check Usuarios: http://localhost:8082/actuator/health

🧪 Datos de Prueba (Seeding)
Al iniciar la aplicación por primera vez, el sistema detecta si las bases de datos están vacías y ejecuta un DataLoader automático que inserta:

Categorías (Tecnología, Hogar, etc.)

Productos con imágenes, precios y descuentos reales.

📝 Notas de Desarrollo
Se implementó un sistema de Healthchecks en Docker Compose para asegurar que las aplicaciones esperen a que las bases de datos estén totalmente listas antes de intentar conectar, evitando errores de Connection Refused.

Uso de DTOs (Data Transfer Objects) para proteger las entidades del dominio y optimizar las respuestas de la API.


📌 Alcance y limitaciones

Backend únicamente (sin frontend)

No incluye API Gateway

No implementa circuit breakers ni retries

Estas decisiones fueron tomadas para priorizar la claridad del backend y la arquitectura base.

👨‍💻 Autor

Anuar Parejo
LinkedIn: https://www.linkedin.com/in/anuar-parejo/
