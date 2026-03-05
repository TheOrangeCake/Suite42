# Transcendence
Last project from 42

* [api42-service documentation](https://github.com/ConzaD/Transcendence/tree/feat-42-Data-Manager/backend/api42-service#api-documentation)


- **auth-service**
  - Handles OAuth2 login with 42
  - Applies access rules (campus, etc.)
  - Issues an internal JWT
  - Stores JWT in an **HttpOnly cookie**
  - Exposes `/api/auth/me`

- **group-service**
  - Business logic (groups, matchmaking, etc.)
  - Does NOT know about OAuth or 42
  - Trusts JWT issued by auth-service
  - Protected by JWT authentication

  - **frontend**
  - Never handles tokens
  - Never talks to 42 directly
  - Uses backend APIs only

1. Frontend redirects user to: /api/auth/oauth2/authorization/42

2. User logs in on 42

3. auth-service:
    - Validates user
    - Issues a JWT
    - Stores it in an HttpOnly cookie (`access_token`)
    - Redirects back to frontend

4. User is now authenticated

**GET /api/auth/me**

Response:
{
  "authenticated": true,
  "login": "login",
  "email": "...",
  "provider": "42"
}