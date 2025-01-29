# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/auth-service.jar auth-service.jar

# Expose the port the application will run on
EXPOSE 9090

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/auth-service.jar"]
