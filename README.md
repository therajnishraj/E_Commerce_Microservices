# Microservices Project

This repository contains a microservices-based application. It includes multiple services that interact with each other via REST APIs and are registered with Eureka Service Discovery. The API Gateway routes requests to the respective microservices.

## Project Structure

### Services
- **User Service:** Handles user-related operations.
- **Order Service:** Manages orders and transactions.
- **Eureka Server:** Service discovery for all microservices.
- **API Gateway:** Routes client requests to appropriate microservices.

## Prerequisites
- Java 17+
- Maven
- Spring Boot 3.0+
- PostgreSQL (for persistence)
- Git (for version control)

## Clone the Repository
```bash
git clone https://github.com/therajnishraj/Microservices.git
cd Microservices
```

## Configuration
Each microservice has its own `application.yml` configuration file. Below are some key configurations:

### User Service (`userapp`)
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

### Order Service (`orderapp`)
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

### Eureka Server
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

### API Gateway
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
  discovery:
    client:
      service-url:
        defaultZone: http://localhost:8761/eureka/

logging:
  file:
    name: apigateway.log
```

## Running the Application
1. **Start Eureka Server:**
   ```bash
   cd eurekaserver
   mvn spring-boot:run
   ```

2. **Start User Service:**
   ```bash
   cd userapp
   mvn spring-boot:run
   ```

3. **Start Order Service:**
   ```bash
   cd orderapp
   mvn spring-boot:run
   ```

4. **Start API Gateway:**
   ```bash
   cd apigateway
   mvn spring-boot:run
   ```

## API Endpoints
### User Service
- `GET /users/getAllUser`: Retrieves all users.

### Order Service
- `GET /orders/getAllOrders`: Retrieves all orders.

### API Gateway Routes
- `http://localhost:8080/users/**`: Routes to User Service.
- `http://localhost:8080/orders/**`: Routes to Order Service.

## Logs
Logs for each service can be found in their respective `*.log` files. For example, API Gateway logs will be stored in `apigateway.log`.

## License
This project is licensed under the MIT License.
