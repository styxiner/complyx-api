# Stage 1
# Compilar app
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /build

# Copiar descripciones de dependencias para cacheo de capa
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Descarga las dependencias 
RUN ./mvnw dependency:go-offline -q

# Copia el codigo fuente y lo compila (saltando los tests de momento)
COPY src ./src
RUN ./mvnw package -DskipTests -q

# Extrae el jar optimizado para docker
RUN java -Djarmode=layertools -jar target/complyx-api-0.0.1-SNAPSHOT.jar extract --destination /build/layers


# Stage 2
# Ejecución
FROM eclipse-temurin:21-jre-alpine AS runtime

RUN addgroup -S complyx && adduser -S complyx -G complyx

WORKDIR /app

# Copiar el contenido del archivo JAR por capas (de más estable a menos estable para optimizar la caché)
COPY --from=builder /build/layers/dependencies ./
COPY --from=builder /build/layers/spring-boot-loader ./
COPY --from=builder /build/layers/snapshot-dependencies ./
COPY --from=builder /build/layers/application ./

# Bloquear el sistema de ficheros

RUN chown -R complyx:complyx /app && chmod -R 550 /app

USER complyx

EXPOSE 8080

# Flags de seguridad en java
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-Djava.security.edg=file:/dev/./urandom", "-Dspring.profiles.active=prod", "-Dserver.port=8080", "org.springframework.boot.loader.launch.JarLauncher"]
