# Management-Tool
ING Hubs assessment

## How to run
```bash
mvn clean install
mvn spring-boot:run
```
Application will start on port 8080.

## Features
- CRUD operations: add, search, list, update price/quantity
- Role-base access control (USER/ADMIN) - Basic Auth
- DTOs, MapStruct, input validation
- Error handling with custom exceptions & global exception handler
- Persistance with H2 (in-memory)
- Logging for business operations
- Unit tests


## Quick API Usage (curl examples)

- Add a product:
```bash
curl -u admin:admin -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Milk","price":5.2,"quantity":15}'
```

- List all products:

```bash
curl -u user:user http://localhost:8080/api/products
```

- Find product by name:

```bash
curl -u admin:admin http://localhost:8080/api/products/Milk
```

- Change product price & quantity:

```bash  
curl -u admin:admin -X PATCH http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{"price":11.99, "quantity":70}'
```
