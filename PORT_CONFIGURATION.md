# Port Configuration Summary

## Microservices Port Assignment (Updated)

### Internal Communication (Docker Network)
| Service | Port | Context Path | Notes |
|---------|------|--------------|-------|
| **api-gateway** | 8080 | - | Reverse proxy for all services |
| **auth-service** | 8081 | `/api/auth` | OAuth2 + JWT authentication |
| **chat-service** | 8082 | `/api/chat` | WebSocket + REST API |
| **group-service** | 8083 | `/api/group` | Group management |
| **api42-service** | 8084 | - | 42 API integration (previously 4444) |
| **regular-user-service** | 8085 | - | Regular user management (previously 4445) |

### External Access (Exposed Ports)
| Source | Port | Target | Via |
|--------|------|--------|-----|
| Browser | 80 | nginx (HTTP) | Redirects to 443 |
| Browser | 443 | nginx (HTTPS) | Reverse proxy |
| Localhost | 3000 | frontend | Vue.js dev server |
| Localhost (debug) | 5432 | PostgreSQL | Shared database |
| Localhost (debug) | 5433 | api42-postgresql | api42 database |
| Localhost (debug) | 5434 | regular-user-postgresql | regular-user database |
| Localhost (debug) | 8084 | api42-service | Direct access (optional) |
| Localhost (debug) | 8085 | regular-user-service | Direct access (optional) |

## Request Flow Examples

### Example 1: Login (OAuth2)
```
Browser → nginx:443 → /api/auth/oauth2/...
         → api-gateway:8080 → auth-service:8081:/api/auth/oauth2/...
         → 42 API (external)
```

### Example 2: Chat Message (REST)
```
Browser → nginx:443 → /api/chat/messages/{id1}/{id2}
        → api-gateway:8080 → chat-service:8082:/api/chat/messages/{id1}/{id2}
        → PostgreSQL:5432
```

### Example 3: WebSocket Connection
```
Browser → nginx:443 → /ws-chat (WebSocket upgrade)
        → api-gateway:8080 → chat-service:8082:/ws-chat (STOMP)
        → PostgreSQL:5432
```

### Example 4: Group Operations
```
Browser → nginx:443 → /api/group/...
        → api-gateway:8080 → group-service:8083:/api/group/...
        → PostgreSQL:5432
```

## Configuration Files Updated

### Service Configurations (YAML/Properties)
```
✅ auth-service/application.yml                    → server.port: 8081
✅ chat-service/application.yml                    → server.port: 8082
✅ group-service/application.yml                   → server.port: 8083
✅ api42-service/application.properties            → server.port: 8084
✅ regular-user-service/application.properties     → server.port: 8085
```

### Gateway Configuration
```
✅ api-gateway/application.yaml
  - auth-service:        http://auth-service:8081
  - chat-service:        http://chat-service:8082
  - group-service:       → group-service:8083
  - api42-service:       http://api42-service:8084
  - chat-socket:         http://chat-service:8082
```

### Docker Compose
```
✅ infra/docker-compose.yml
  - api42-service ports: 4444:4444   → 8084:8084
  - regular-user-service ports: 4445:4445 → 8085:8085
```

### Image Domain References Updated
```
✅ api42-service/application.properties
   api42.image-domain: http://localhost:4444/images42/   → http://localhost:8084/images42/

✅ regular-user-service/application.properties
   regUser.image-domain: http://localhost:4445/images-regular/   → http://localhost:8085/images-regular/
```

## Testing Port Configuration

### Direct Service Access (for debugging)
```bash
# Check individual services are running on correct ports
curl http://localhost:8081/api/auth/health       # auth-service
curl http://localhost:8082/api/chat/health       # chat-service
curl http://localhost:8083/api/group/health      # group-service
curl http://localhost:8084/health                # api42-service
curl http://localhost:8085/health                # regular-user-service
```

### Via Gateway (internal Docker network - no external access)
```bash
# Only works within docker containers or if gateway ports are exposed
curl http://localhost:8080/api/auth/health
curl http://localhost:8080/api/chat/health
curl http://localhost:8080/api/group/health
```

### Via Nginx (external access - recommended)
```bash
# Access through reverse proxy (secure)
curl https://localhost/api/auth/health
curl https://localhost/api/chat/health
curl https://localhost/api/group/health
```

## Docker Compose Network

All services communicate via the internal Docker network `app_net`:
```
┌─────────────────────────────────────────────────────────────┐
│                     app_net (bridge)                        │
├─────────────────────────────────────────────────────────────┤
│  ┏━━━━━━━━━━━━┓  ┏━━━━━━━━━━━━┓  ┏━━━━━━━━━━━━┓           │
│  ┃   nginx    ┃  ┃ api-gateway┃  ┃ postgres   ┃           │
│  ┃   :80/443  ┃  ┃   :8080    ┃  ┃  :5432    ┃           │
│  ┗━━━━━━━━━━━━┛  ┗━━━━━━━━━━━━┛  ┗━━━━━━━━━━━━┛           │
│       ↓                 ↓             ↑                     │
│  ┌────────────────────────────────────────────────────┐   │
│  │  auth-service:8081  chat-service:8082             │   │
│  │  group-service:8083  api42-service:8084           │   │
│  │  regular-user-service:8085                        │   │
│  └────────────────────────────────────────────────────┘   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
     ↑                                        ↓
External clients (browser, localhost tools)  PostgreSQL databases
```

## Rollback Information

If rollback is needed, the original port assignments were:
- api42-service: 4444
- regular-user-service: 4445
- All others: 8080

To revert:
1. Change ports back in service `.yml` or `.properties` files
2. Update API Gateway routes back to old URIs
3. Update docker-compose.yml port mappings
4. Update image-domain references

---
**Last Updated**: Phase 2 Port Unification  
**Status**: All services assigned unique ports (8081-8085)
