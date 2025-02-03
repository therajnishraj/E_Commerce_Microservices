# Stage 1: Build the application with Maven and OpenJDK 17
FROM maven:3.9.7-eclipse-temurin-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven configuration files
COPY pom.xml ./
COPY .mvn .mvn

# Copy the source code
COPY src src

## Build the project with the 'docker' profile and create the executable JAR
#RUN mvn clean package -DskipTests -Dspring.profiles.active=docker

# Build the project with the 'docker' profile and create the executable JAR
RUN mvn clean package -DskipTests -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-docker}

# Stage 2: Run the application using OpenJDK 17
FROM eclipse-temurin:17-jdk

# Copy the built JAR file from the build stage
COPY --from=build /app/target/auth-service.jar /app/auth-service.jar

# Expose the port the application will run on
EXPOSE 9090

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/auth-service.jar"]
