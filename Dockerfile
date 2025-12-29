# Etapa 1: Build
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Argumento para saber qué módulo compilar (valor por defecto)
ARG MODULE_NAME=msvc-producto

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
# Copiamos todos los módulos para resolver dependencias entre ellos
COPY . .

# Compilamos solo el módulo solicitado y sus dependencias necesarias
RUN ./mvnw clean package -DskipTests -pl ${MODULE_NAME} -am

# Etapa 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

ARG MODULE_NAME=msvc-producto
# Copiamos el JAR generado dinámicamente
COPY --from=build /app/${MODULE_NAME}/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]