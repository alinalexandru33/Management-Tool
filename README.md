# Management-Tool
ING Hubs assessment


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
curl -u admin:admin -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Milk","price":5.2,"quantity":15}'

- List all products:
curl -u user:user http://localhost:8080/api/products

- Find product by name:
curl -u admin:admin http://localhost:8080/api/products/Milk

- Change product price:
curl -u admin:admin -X PUT http://localhost:8080/api/products/1/price \
  -H "Content-Type: application/json" \
  -d '6.99'

- Change product quantity:
curl -u admin:admin -X PUT http://localhost:8080/api/products/1/quantity \
  -H "Content-Type: application/json" \
  -d '30'
