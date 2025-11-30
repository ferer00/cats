# --- Stage 1: Build ---
FROM gradle:8.7-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# --- Stage 2: Run ---
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy fat JAR
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
