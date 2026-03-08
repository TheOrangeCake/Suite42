# PHASE 2.5 - End-to-End Testing Guide

## Overview
This guide validates that all Phase 2 changes are working correctly:
- ✅ Exception handling (ChatControllerAdvice)
- ✅ Security config + JWT filter
- ✅ API Gateway routing for chat-service
- ✅ Nginx WebSocket headers

## Prerequisites
- Docker & Docker Compose installed
- Valid `.env` file in `/infra/` with:
  - `JWT_SECRET=<min 32 chars>`
  - `POSTGRES_PASSWORD=<password>`
  - `FORTYTWO_CLIENT_ID=<id>`
  - `FORTYTWO_CLIENT_SECRET=<secret>`

## Test Procedure

### Step 1: Build and Start Services
```bash
cd /home/dimitri/TMAX/Dev/infra
docker-compose up --build
```

Wait 30-60 seconds for all services to start. Look for:
- ✅ `chat-service is running` (logs show startup complete)
- ✅ `api-gateway is running` (Spring Cloud Gateway started)
- ✅ `nginx started` (reverse proxy listening on 80/443)

### Step 2: Health Check Endpoints

#### 2.1 Chat Service Direct Health (direct, no auth required)
```bash
curl -X GET http://localhost:8080/api/chat/health
# Expected: 200 OK or similar success response
```

#### 2.2 Chat Service Through Gateway
```bash
curl -X GET http://localhost:80/api/chat/health
# Expected: 200 OK (routed through nginx → api-gateway → chat-service)
```

#### 2.3 Check Health Actuator
```bash
curl -X GET http://localhost:80/api/chat/info
# Expected: 200 OK with service info
```

### Step 3: JWT Token Validation

#### 3.1 Get JWT Token from Auth Service
```bash
# Login via OAuth2 42 (requires browser)
# OR use direct endpoint if exposed
curl -X POST http://localhost:80/api/auth/oauth2/code/42 \
  -H "Content-Type: application/json"
  
# Copy the access_token from response
export JWT_TOKEN="<your_token_here>"
```

#### 3.2 Test Protected REST Endpoint (Chat History)
```bash
# This endpoint requires JWT validation (Phase 2.2)
curl -X GET http://localhost:80/api/chat/messages/user1/user2 \
  -H "Authorization: Bearer $JWT_TOKEN"
  
# Expected: 
# - 200 OK if token valid (may be empty message list)
# - 401 UNAUTHORIZED if token invalid or missing
```

#### 3.3 Test Missing Token (Error Handling - Phase 2.1)
```bash
curl -X GET http://localhost:80/api/chat/messages/user1/user2

# Expected: 401 UNAUTHORIZED with error response:
# {"error": "Authentication failed: ..."}
```

### Step 4: WebSocket Connection Test

#### 4.1 Test WebSocket Upgrade Through Nginx
```bash
# Using wscat or similar WebSocket client
npm install -g wscat

wscat -c "ws://localhost:80/ws-chat"
# Expected connection... (may fail if no auth yet)
```

#### 4.2 Test WebSocket with Bearer Token
```bash
wscat -c "ws://localhost:80/ws-chat" \
  -H "Authorization: Bearer $JWT_TOKEN"
  
# Once connected, send STOMP CONNECT frame:
{"action":"CONNECT","headers":{"Authorization":"Bearer $JWT_TOKEN"}}

# Expected: Server accepts connection and sets authenticated user
```

### Step 5: Error Handling Validation (Phase 2.1 - ChatControllerAdvice)

#### 5.1 Invalid JWT Error
```bash
curl -X GET http://localhost:80/api/chat/messages/user1/user2 \
  -H "Authorization: Bearer invalid_token_12345"
  
# Expected: 401 with JSON:
# {"error":"JWT_VALIDATION_FAILED","message":"Invalid or expired token",...}
```

#### 5.2 Malformed Request Error  
```bash
# Send message with missing field (if endpoint exposed)
curl -X POST http://localhost:80/api/chat/send \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"content":"hello"}'
  
# Expected: 400 BAD_REQUEST with error details
```

### Step 6: Verify JWT Secret Synchronization

#### 6.1 Check Auth Service JWT Generation
```bash
curl http://localhost:80/api/auth/debug/jwt \
  -b "access_token=$JWT_TOKEN"
  
# View decoded JWT claims
```

#### 6.2 Verify Same Secret in All Services
All three microservices should use the SAME `JWT_SECRET`:
- ✅ auth-service: `${app.jwt.secret}` → `${JWT_SECRET}`
- ✅ chat-service: `${app.jwt.secret}` → `${JWT_SECRET}`
- ✅ group-service: `${app.jwt.secret}` → `${JWT_SECRET}`

Check logs:
```bash
docker logs fronta-chat-service | grep "JWT_SECRET"
docker logs fronta-auth-service | grep "JWT_SECRET"
```

### Step 7: Verify API Gateway Routes

#### 7.1 Check Route Configuration
```bash
curl http://localhost:8080/actuator/mappings | grep -A 5 "chat"
# Should show routes for /api/chat and /ws-chat
```

#### 7.2 Trace Request Through Gateway
Monitor logs while making requests:
```bash
docker logs fronta-api-gateway | tail -20
# Look for routing decisions and path transformations
```

### Step 8: Verify Nginx Configuration

#### 8.1 Check Nginx Proxy Headers for WebSocket
```bash
# Check nginx config mounted correctly
docker exec fronta-nginx cat /etc/nginx/conf.d/default.conf | grep -A 10 "ws-chat"

# Expected: Upgrade and Connection headers present
```

#### 8.2 Test Nginx Reverse Proxy
```bash
curl -v http://localhost:80/api/chat/health
# Expected: See `X-Forwarded-Proto: https` in response headers
```

## Success Criteria

| Component | Criterion | Status |
|-----------|-----------|--------|
| Chat Service Startup | Service starts without errors | [ ] |
| Health Endpoint | Returns 200 OK (no auth required) | [ ] |
| REST Endpoint Auth | 401 without JWT, 200 with valid JWT | [ ] |
| Error Handling | Proper JSON error responses | [ ] |
| JWT Validation | Accepts valid token, rejects invalid | [ ] |
| API Gateway Routes | Requests routed correctly to chat-service | [ ] |
| WebSocket Connection | Can upgrade to WebSocket protocol | [ ] |
| WebSocket Auth | WebSocket validates JWT in headers | [ ] |
| Nginx Proxy | Requests flow through nginx reverse proxy | [ ] |

## Troubleshooting

### Issue: Chat-service fails to start
- Check logs: `docker logs fronta-chat-service`
- Verify JWT_SECRET env var is set (min 32 chars)
- Check database connectivity: `docker logs fronta-postgres`

### Issue: 502 Bad Gateway
- API Gateway can't reach chat-service
- Check docker network: `docker network ls`
- Verify service DNS name: `ping chat-service` from gateway container

### Issue: JWT validation fails
- Check JWT_SECRET matches across services
- Verify token not expired (2-hour TTL)
- Check Authorization header format: `Bearer <token>`

### Issue: WebSocket won't connect
- Check CORS headers (nginx configuration)
- Verify WebSocket protocol upgrade headers sent by browser
- Check nginx buffering disabled: `proxy_buffering off;`

## Next Steps

After successful testing:
1. **Phase 3**: Integrate frontend WebSocket client
2. **Phase 4**: Full end-to-end chat flow (send/receive messages)
3. **Phase 5**: Deployment to production

---
**Last Updated**: Phase 2 Complete  
**Status**: Ready for comprehensive testing
