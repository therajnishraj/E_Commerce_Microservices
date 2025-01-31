# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/order-service.jar /app/order-service.jar

# Expose the port the application will run on
EXPOSE 8082

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/order-service.jar"]
