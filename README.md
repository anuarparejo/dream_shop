# 🛒 DreamShop - Microservices Architecture

DreamShop es una plataforma de e-commerce desarrollada con una arquitectura de microservicios robusta, diseñada para ser escalable, fácil de mantener y lista para entornos productivos mediante contenedores.

## 🚀 Tecnologías Principales

* **Java 21** (Optimizado para alto rendimiento)
* **Spring Boot 3.4.1**
* **Spring Data JPA**
* **Bases de Datos Políglotas:** * **PostgreSQL 16** (Microservicio de Productos)
    * **MySQL 8.0** (Microservicio de Usuarios)
* **Docker & Docker Compose** (Orquestación de infraestructura)
* **Lombok & MapStruct** (Código limpio y mapeo eficiente)

---

## 🏗️ Arquitectura del Sistema

El proyecto se divide en módulos independientes que se comunican de forma desacoplada, cada uno con su propio ciclo de vida y almacenamiento:

1.  **msvc-productos:** Gestiona el catálogo, categorías y stock. Utiliza PostgreSQL por su robustez en integridad de datos.
2.  **msvc-usuarios:** Administra el registro, perfiles y seguridad de los clientes. Utiliza MySQL.



---

## 🛠️ Configuración y Despliegue con Docker

Gracias a Docker, no necesitas instalar las bases de datos localmente. Todo el entorno se levanta con un solo comando.

### Requisitos previos
* Docker Desktop instalado.
* Maven 3.9+ (o usar el wrapper `./mvnw`).

### Pasos para ejecutar:

1. **Clonar el repositorio:**
   ```bash
   git clone [https://github.com/tu-usuario/DreamShop.git](https://github.com/tu-usuario/DreamShop.git)
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

Creado por Anuar Parejo - https://www.linkedin.com/in/anuar-parejo/
