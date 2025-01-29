# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/eureka-service.jar eureka-service.jar

# Expose the port the application will run on
EXPOSE 8761

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/eureka-service.jar"]
