# Ecommerce Microservices Platform

A production-style microservices backend demonstrating distributed systems design: event-driven communication, polyglot persistence, caching, API gateway routing, containerization, and CI/CD.

## Architecture

```
                         ┌────────────────┐
                         │  API Gateway    │  (Spring Cloud Gateway :8080)
                         └───────┬─────────┘
                  ┌──────────────┼──────────────┐
                  ▼                              ▼
        ┌──────────────────┐          ┌──────────────────┐
        │ Product Service    │          │ Order Service      │
        │ (Spring Boot :8081)│          │ (Spring Boot :8082)│
        │ MongoDB + Redis    │          │ PostgreSQL         │
        └─────────┬─────────┘          └─────────┬─────────┘
                  │                              │
                  └──────────► Kafka ◄────────────┘
                          (order-events topic)
```

## Why these choices

- **MongoDB for catalog**: flexible schema for varying product attributes.
- **PostgreSQL for orders**: relational integrity for transactional order data.
- **Redis**: caches hot product reads to cut DB load.
- **Kafka**: decouples order placement from downstream inventory/notification handling — services stay independently scalable and resilient to each other's downtime.
- **Spring Cloud Gateway**: single entry point, centralizes routing/auth concerns.

## Services

| Service | Port | Datastore | Responsibility |
|---|---|---|---|
| product-service | 8081 | MongoDB, Redis | Catalog CRUD, cached reads |
| order-service | 8082 | PostgreSQL | Order placement, publishes `order-events` to Kafka |
| api-gateway | 8080 | — | Routes `/api/products/**`, `/api/orders/**` |

## Run locally

```bash
docker-compose up --build
```

This starts Kafka, MongoDB, PostgreSQL, Redis, and all three services.

- Product API: http://localhost:8080/api/products
- Order API: http://localhost:8080/api/orders
- Swagger (product): http://localhost:8081/swagger-ui.html

## Roadmap / next increments

- [ ] Payment service (mock external gateway)
- [ ] Inventory service consuming `order-events`
- [ ] Terraform module to provision ECS + RDS
- [ ] Prometheus/Grafana observability stack
- [ ] JWT auth at the gateway

## Design decisions log

See commit history — each service and cross-cutting concern (caching, eventing, gateway routing) was added as a separate, reviewable commit rather than one large drop.
