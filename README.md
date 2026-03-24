# Perfume Marketplace

## Overview

This project is a **TPO (Trabajo Práctico Obligatorio)** developed for the subject **Aplicaciones Interactivas** at **UADE (Universidad Argentina de la Empresa)**.

The goal is to build an e-commerce platform focused on selling **perfume samples** — initially targeting 5ml and 10ml formats. The platform allows users to act either as sellers, listing their samples for sale, or as buyers, browsing and purchasing available products.

## Team

| Name | Role |
|---|---|
| Luca Cevasco | Member |
| Abril Herrero | Member |
| Martina Juarez | Member |
| Federico Sola | Member |

## Tech Stack

- **Java 21** with **Spring Boot 4** — REST API backend
- **Spring Data JPA** — Database access layer
- **MySQL 8.0** — Relational database
- **Docker Compose** — Database containerization

---

## Database Setup

The project uses **MySQL 8.0** running in a Docker container, managed automatically by Spring Boot's Docker Compose integration.

### Prerequisites

- **Java 21** or later
- **Maven** (or use the included `./mvnw` wrapper)
- **Docker Desktop** installed and running — [Download here](https://www.docker.com/products/docker-desktop/)

### Running the Application

Simply start the Spring Boot application:

```bash
./mvnw spring-boot:run
```

Spring Boot will automatically:
1. Detect the `compose.yaml` file at the project root
2. Run `docker compose up` to start the MySQL container
3. Auto-configure the JDBC connection (no manual config needed)
4. Create/update the database schema via Hibernate
5. Run `docker compose stop` when the application shuts down (data is preserved)

**No manual `docker compose up` needed** — it's all handled by the `spring-boot-docker-compose` dependency.

### Automatic Data Seeding

On first startup, the application automatically seeds the database with ~50 perfumes from the [Fragella API](https://api.fragella.com), fetching data for popular brands (Dior, Chanel, YSL, Tom Ford, Versace, Armani, Creed, Jean Paul Gaultier, Prada, Dolce & Gabbana).

Each perfume includes detailed information: ratings, notes (top/middle/base), accords, season and occasion rankings, images, and more.

The seeder only runs when the database is empty — on subsequent restarts it is skipped automatically. To re-seed, reset the database first (see [Resetting the Database](#resetting-the-database)).

### Testing the API

Once the application is running, you can test the perfume endpoints:

**Create a perfume:**
```bash
curl -X POST http://localhost:8080/perfume \
  -H "Content-Type: application/json" \
  -d '{"name": "Sauvage", "brand": "Dior", "line": "Sauvage", "description": "Fragancia fresca y especiada", "releaseYear": 2015}'
```

**Get all perfumes:**
```bash
curl http://localhost:8080/perfume/all
```

**Update a perfume:**
```bash
curl -X PUT http://localhost:8080/perfume/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "Sauvage Elixir", "brand": "Dior", "line": "Sauvage", "description": "Versión concentrada", "releaseYear": 2021}'
```

**Delete a perfume:**
```bash
curl -X DELETE http://localhost:8080/perfume/1
```

### Connecting to the Database Manually

To inspect the database with a SQL client (e.g., MySQL Workbench, DBeaver):

| Setting | Value |
|---|---|
| Host | `localhost` |
| Port | `3306` |
| Database | `marketplace_perfume` |
| Username | `marketplace` |
| Password | `marketplace123` |

### Running Without Docker Compose Support

If you prefer to run MySQL manually or connect to an external instance:

1. Start the MySQL container yourself:
   ```bash
   docker compose up -d
   ```

2. Uncomment the datasource properties in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/marketplace_perfume
   spring.datasource.username=marketplace
   spring.datasource.password=marketplace123
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   ```

3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

### Resetting the Database

To completely reset the database and start fresh:

```bash
docker compose down -v
```

This removes the container and the data volume. The next time you start the app, a fresh database will be created.

