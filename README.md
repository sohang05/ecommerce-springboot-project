# Ecommerce Project

## Overview
This is a complete Ecommerce web application developed using **Spring Boot** and **Java**. The application features user registration, product management, shopping cart, and order placement functionalities. It includes JWT-based security, Swagger for API documentation, Eureka for service registration, and Actuator for monitoring.

### Key Features:
- **JWT Security**: Secures user login and authentication.
- **Swagger**: Provides an interactive API documentation interface.
- **Eureka**: Handles service registration and discovery.
- **Actuator**: Monitors and manages the application.
- **MySQL Database**: Used for storing user and product data.

## Tech Stack:
- **Backend**: Java, Spring Boot, Spring Security (JWT)
- **Database**: MySQL
- **Service Discovery**: Eureka
- **API Documentation**: Swagger
- **Monitoring**: Spring Boot Actuator
- **Build Tool**: Maven

## Requirements:
- Java 17+
- MySQL 8.0+
- Maven 3.6+
- Postman (for testing API endpoints)

## Setup Instructions:
1. Clone the repository:
   ```bash
   git clone https://github.com/username/ecommerce-project.git
   ```

2. Navigate to the project directory:
```
cd ecommerce-project
```

3. Update application.properties with your MySQL credentials:
```
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=your-username
spring.datasource.password=your-password
```

4. Run the application:
```
mvn spring-boot:run
```

### API Documentation:
- The Swagger UI is available at ```http://localhost:8080/swagger-ui.html``` for interactive API exploration.

### Testing:
- Test the endpoints using Postman or Swagger UI. Sample endpoints:

* User Registration: ```POST /api/auth/register```
* User Login: ```POST /api/auth/login```
* Get Products: ```GET /api/products```
* Place Order: ```POST /api/orders```

### Eureka Dashboard:
- The Eureka dashboard can be accessed at ```http://localhost:8761```

### Actuator:
- Actuator endpoints (e.g., health check) are available at ```http://localhost:8080/actuator```

Contributing:
Feel free to submit a pull request or raise an issue if you find any bugs or want to contribute to the project.

License:
This project is licensed under the MIT License


##### Happy Coding!!!
