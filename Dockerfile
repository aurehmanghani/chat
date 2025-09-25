# Stage 1: Build the JAR inside Docker
FROM maven:3.9.2-eclipse-temurin-8 AS build
WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY src ./src

# Build the JAR
RUN mvn clean package -DskipTests

# Stage 2: Run the Spring Boot app
FROM openjdk:8-jdk-alpine
VOLUME /tmp

# Copy JAR from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 3000

ENTRYPOINT ["java", "-jar", "/app.jar"]
