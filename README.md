# Inventory Management System

REST API for inventory management with product control, stock lots, and stock movements, implementing FEFO (First Expired, First Out) logic for automatic lot consumption.

---

## Technologies

- Java 21
- Spring Boot 3.5
- Spring Data JPA / Hibernate
- MySQL
- Flyway
- MapStruct
- Bean Validation
- SpringDoc OpenAPI (Swagger)

---

## Architecture

The project follows a layered architecture:

```
Controller → Service → Repository → Entity
```

- **Controller** — receives HTTP requests and delegates to the service layer
- **Service** — contains business rules
- **Repository** — database access via Spring Data JPA
- **Entity** — JPA-mapped domain classes
- **DTO** — data transfer objects for request/response, decoupled from entities
- **Mapper** — MapStruct-generated converters between entities and DTOs
- **Exception Handler** — `@ControllerAdvice` with standardized error responses

---

## FEFO Logic

When a stock movement of type `OUT` is registered, the system automatically consumes the lots with the earliest expiry date first. The consumption is tracked in the `stock_movement_lots` table, recording exactly which lots were consumed in each movement.

---

## Database

The schema is managed by Flyway. Tables:

- `products` — product catalog
- `stock_lots` — stock lots with batch code and expiry date
- `stock_movements` — records of stock entries, exits and adjustments
- `stock_movement_lots` — links movements to the lots consumed (FEFO tracking)

---

## Endpoints

### Products — `/products`

| Method | Path | Description |
|--------|------|-------------|
| POST | `/products` | Create a new product |
| GET | `/products` | List all products |
| GET | `/products/{id}` | Find product by ID |
| GET | `/products/filter` | Search products by name or SKU (paginated) |
| PUT | `/products/{id}` | Update product data |
| PATCH | `/products/{id}/status` | Enable or disable a product |

### Stock Lots — `/stock-lots`

| Method | Path | Description |
|--------|------|-------------|
| POST | `/stock-lots` | Create a new stock lot |
| GET | `/stock-lots` | List all stock lots |
| GET | `/stock-lots/{id}` | Find stock lot by ID |
| GET | `/stock-lots/{id}/stock` | Get total stock quantity of a product |

### Stock Movements — `/stock-movements`

| Method | Path | Description |
|--------|------|-------------|
| POST | `/stock-movements` | Register a stock movement |
| GET | `/stock-movements` | List all movements |
| GET | `/stock-movements/{id}` | Find movement by ID |
| GET | `/stock-movements/search` | Filter movements by product, type, reason and date range (paginated) |

---

## Movement Types and Reasons

| Type | Allowed Reasons |
|------|----------------|
| `IN` | `PURCHASE`, `INITIAL_STOCK`, `RETURN_FROM_CLIENT` |
| `OUT` | `SALE`, `EXPIRED`, `DAMAGE` |
| `ADJUST` | `ADJUSTMENT_IN`, `ADJUSTMENT_OUT` |

---

## Error Response

All errors follow a standardized format:

```json
{
  "timestamp": "2026-03-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found with ID: 5",
  "path": "/products/5"
}
```

---

## Setup

### Requirements

- Java 21+
- MySQL 8+
- Maven

### Steps

1. Clone the repository:
```bash
git clone https://github.com/leoferreira9/inventory_management_system.git
cd inventory_management_system
```

2. Create the database in MySQL:
```sql
CREATE DATABASE inventory_management_db;
```

3. Copy the example properties file and fill in your credentials:
```bash
cp src/main/resources/application-example.properties src/main/resources/application.properties
```

4. Edit `application.properties` with your MySQL username and password:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/inventory_management_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

5. Run the application:
```bash
./mvnw spring-boot:run
```

Flyway will automatically create all tables on first run.

---

## API Documentation

After starting the application, access the Swagger UI at:

```
http://localhost:8080/swagger-ui/index.html
```
