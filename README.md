# Transcendence – Project Overview

This repository is organized as a small microservices project.
Everything is started with **Docker Compose** and the backend is accessed through an **API Gateway**.

## 1) Main folders

### `infra/`
Infrastructure setup (this is where the project is “wired”).
- **`infra/docker-compose.yml`** ✅  
  The main file that declares all containers (frontend, gateway, services, databases), ports, networks, env vars.
- `infra/nginx/nginx.conf`  
  Old / optional nginx reverse-proxy config, this is currently commented in compose.

### `api-gateway/`

The API Gateway (Spring Boot + Spring Cloud Gateway).
- **`api-gateway/src/main/resources/application.yaml`** ===> Gateway routing rules (ex: `/api/auth/**` → auth-service).
- `api-gateway/pom.xml`  ==> Java dependencies.


### `backend/`

All backend microservices (each service is its own Spring Boot project).
- `backend/auth-service/`
- `backend/group-service/`
- `backend/api42-service/` ==>  [api42-service documentation](./backend/api42-service/README.md)
- `backend/regular-user-service/` ==> [regular-user-service documentation](./backend/regular-user-service/README.md)

### `chat-service/`
Another Spring Boot service (separate folder, not inside `backend/` (maybe we gonna move it later)).

### `frontend/`
Frontend app (Vue/Vite) built into static files and served by Nginx.
- `frontend/containers/Dockerfile` (build frontend + run nginx)
- `frontend/ressources/nginx.conf` (nginx config for SPA routing)
- `frontend/design` ==> [design documentation](./frontend/design/README.md)

Each service folder usually contains:
- `pom.xml` (Java dependencies)
- `Dockerfile` (build + run container)
- `src/main/resources/application.yml` (service config, port, etc.)


## 2) Where services are declared (most important file)

✅ **`infra/docker-compose.yml`**
- declares containers
- sets ports (what is exposed to your machine)
- sets networks (how containers talk to each other)
- injects environment variables from `.env`

Run with:
    make
