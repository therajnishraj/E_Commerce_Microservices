# E-Commerce Microservices Architecture

This repository contains a microservices-based application. It includes multiple services that interact with each other via FeignClient and are registered with Eureka Service Discovery. The API Gateway routes requests to the respective microservices.

## Architecture Overview

This application is designed using Spring Boot Microservices and includes the following components:

- **Authentication Service**: Handles user authentication and generates JWT tokens.
- **User Service**: Manages user-related operations such as user registration, profile management, etc.
- **Order Service**: Manages order creation, transactions, and processing.
- **API Gateway**: Acts as a single entry point, routing requests to the respective microservices while validating authentication tokens.
- **Eureka Server**: Service discovery mechanism that allows services to register and communicate dynamically.

## Workflow

1. Client sends a request to the API Gateway.
2. If authentication is required, the API Gateway forwards the credentials to the Auth Service for validation.
3. Once validated, the Auth Service generates a JWT token and returns it to the client.
4. The client includes the JWT token in subsequent requests.
5. The API Gateway checks the token before forwarding requests to respective services:
    - **User Service**: If the request is user-related.
    - **Order Service**: If the request is order-related.
6. If a service is down, a fallback method is triggered to handle the failure gracefully.
## Architecture Diagram

![Ecart Architecture Diagram](https://github.com/therajnishraj/Microservices/blob/main/ecart_diagram.jpeg)


## Microservices Communication

- Each microservice registers with **Eureka Server**, allowing dynamic service discovery.
- **API Gateway** routes traffic, ensuring seamless communication.
- Filters (such as authentication and request validation) are applied before forwarding requests to the services.

## Technology Stack

- **Spring Boot**: Microservices Framework
- **Spring Cloud Gateway**: API Gateway
- **Spring Security & JWT**: Authentication
- **Spring Cloud Eureka**: Service Discovery
- **PostgreSQL**: Database
- **Feign Client**: Inter-Service Communication
- **Resilience4j**: Circuit Breaker & Fallback Handling
- **Docker & Docker Compose**: Containerization & Deployment


## Project Structure


---

## Prerequisites
- **Java 17+**
- **Maven**
- **Spring Boot 3.0+**
- **PostgreSQL** (for persistence)
- **Git** (for version control)
- **Docker & Docker Compose** (for containerization)

---

## Clone the Repository
```bash
git clone https://github.com/therajnishraj/Microservices.git
cd Microservices
```

---
## Running with Docker

### **Docker Setup**
1. **Ensure Docker is installed and running on your machine.**
2. **Build the Docker images for each microservice:**
   ```bash
   docker build -t user-service ./userapp
   docker build -t order-service ./orderapp
   docker build -t auth-service ./authapp
   docker build -t api-gateway ./apigateway
   docker build -t eureka-server ./eurekaserver
   ```

3. **Run the services using Docker Compose:**
   ```bash
    # Build the images defined in docker-compose.yml
    docker compose build  

    # Start the containers as per docker-compose.yml (in detached mode if needed: add -d)
    docker-compose up  

    # Stop and remove containers, networks, and volumes created by docker-compose up
    docker compose down  

    # List all running and stopped containers
    docker ps -a  

    # List only running containers
    docker ps  

    # Remove unused build cache to free up space
    docker builder prune -a  

    # View logs of a specific container (replace with actual container ID)
    docker logs 44ed2a3b8ef9  

    # Run a specific container (useful for restarting or testing manually)
    docker run ecart_microservice-user-service-1  

    # List all Docker images
    docker images  

    # Remove a specific Docker image (replace with actual image name or ID)
    docker rmi order-service  

    # Remove a specific stopped container (replace with actual container ID)
    docker rm 13dabc72b8ae  

   ```

### **Docker Compose File (`docker-compose.yml`)**

## Configuration

Each microservice has its own `application.yml` configuration file. Below are some key configurations:

### **User Service (userapp)**
```yaml
server:
  port: 8081

spring:
  application:
    name: USERAPP
  datasource:
    url: jdbc:postgresql://localhost:5432/users_db
    username: your_username
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

### **Order Service (orderapp)**
```yaml
server:
  port: 8082

spring:
  application:
    name: ORDERAPP
  datasource:
    url: jdbc:postgresql://localhost:5432/orders_db
    username: your_username
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

### **Eureka Server**
```yaml
server:
  port: 8761

spring:
  application:
    name: EUREKASERVER

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
  server:
    enable-self-preservation: false
```

### **API Gateway**
```yaml
server:
  port: 8080

spring:
  application:
    name: api-gateway
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://USERAPP
          predicates:
            - Path=/users/**
        - id: order-service
          uri: lb://ORDERAPP
          predicates:
            - Path=/orders/**
        - id: auth-service
          uri: lb://AUTHSERVICE
          predicates:
            - Path=/auth/**
  discovery:
    client:
      service-url:
        defaultZone: http://localhost:8761/eureka/

logging:
  file:
    name: apigateway.log
```

---

## Running the Application

1. **Start Eureka Server**:
   ```bash
   cd eurekaserver
   mvn spring-boot:run
   ```

2. **Start Auth Service**:
   ```bash
   cd authapp
   mvn spring-boot:run
   ```

3. **Start User Service**:
   ```bash
   cd userapp
   mvn spring-boot:run
   ```

4. **Start Order Service**:
   ```bash
   cd orderapp
   mvn spring-boot:run
   ```

5. **Start API Gateway**:
   ```bash
   cd apigateway
   mvn spring-boot:run
   ```

---

## Workflow

1. **Authentication**:
   - A user registers or logs in through the **Auth Service**, which generates a JWT token upon successful authentication.
   - The token is passed back to the client.

2. **API Gateway**:
   - The client sends requests to the API Gateway, including the JWT token in the `Authorization` header.
   - The **API Gateway** validates the token using a custom authentication filter before routing the request to the appropriate service.

3. **User and Order Services**:
   - The **User Service** and **Order Service** are protected by JWT filters to ensure only authenticated requests are processed.
   - **Order Service** communicates with the **User Service** via a Feign Client, passing the JWT token for authorization.

4. **Service Discovery**:
   - All services are registered with **Eureka Server**, which allows the API Gateway to dynamically discover service instances.

---

## Diagram

```plaintext
 [Client]
    |
    v
[API Gateway] <-----> [Eureka Server]
    |
    +--------------------------------+
    |                                |
    v                                v
[User Service] <------> [Order Service]
    ^                                ^
    |                                |
    v                                |
 [Auth Service] <--------------------+
```

---



## API Endpoints

### **Auth Service**
- **Register User**:
  ```bash
  curl --location 'http://localhost:8080/auth/register' \
  --header 'Content-Type: application/json' \
  --data-raw '{
      "name": "rajnish",
      "email": "raj@gmail.com",
      "password": "raja"
  }'
  ```

- **Generate Token**:
  ```bash
  curl --location 'http://localhost:8080/auth/token' \
  --header 'Content-Type: application/json' \
  --data '{
      "username": "rajnish",
      "password": "raja"
  }'
  ```

### **User Service**
- **Get All Users**:
  ```bash
  curl --location 'http://localhost:8080/users/getAllUser' \
  --header 'Authorization: Bearer <your-jwt-token>'
  ```

### **Order Service**
- **Get Order by ID**:
  ```bash
  curl --location 'http://localhost:8080/orders/getOrderById/2' \
  --header 'Authorization: Bearer <your-jwt-token>'
  ```

- **Create Order**:
  ```bash
  curl --location --request POST 'http://localhost:8080/orders/order/1/user/1' \
  --header 'Content-Type: application/json' \
  --header 'Authorization: Bearer <your-jwt-token>' \
  --data '{
      "product": "Iphone",
      "quantity": 3,
      "price": 80000
  }'
  ```

---

## Logs

Logs for each service can be found in their respective `*.log` files. For example, API Gateway logs will be stored in `apigateway.log`.

---

## License

This project is licensed under the [MIT License](LICENSE).

