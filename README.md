# Intelligent Enterprise Operations & Decision Platform (IEODP)

## Overview

IEODP is a production-grade Java backend system built with Spring Boot, implementing enterprise-level features including:

- **JWT Authentication with Refresh Tokens**: Secure stateless authentication
- **Role-Based Access Control (RBAC)**: Fine-grained permissions system
- **Workflow State Machine**: Business process management with state transitions
- **Comprehensive Audit Logging**: Full traceability and compliance
- **RESTful API**: Versioned APIs with OpenAPI/Swagger documentation
- **Enterprise Data Handling**: Pagination, sorting, filtering, optimized queries

## Technology Stack

- **Java 17+**
- **Spring Boot 4.0.1**
- **Spring Security** with JWT
- **Spring Data JPA**
- **MySQL/PostgreSQL**
- **Maven**
- **OpenAPI/Swagger**
- **Lombok**

## Project Architecture

The project follows **Layered Architecture** with **Domain-Driven Design (DDD)** principles:

```
com.ieodp
├── config          # Configuration classes
│   ├── security    # Security configuration
│   ├── swagger     # API documentation
│   └── logging     # Logging configuration
├── common          # Shared components
│   ├── exception   # Custom exceptions
│   ├── response    # Standardized responses
│   ├── util        # Utility classes
│   └── domain      # Base entities
├── security        # Security components
│   ├── jwt         # JWT implementation
│   ├── filter      # Security filters
│   └── service     # Security services
├── user            # User management module
│   ├── controller  # REST controllers
│   ├── service     # Business logic
│   ├── domain      # Domain entities
│   ├── repository  # Data access
│   └── dto         # Data transfer objects
├── workflow        # Workflow management module
│   ├── controller
│   ├── service
│   ├── domain
│   └── repository
├── audit           # Audit logging module
│   ├── domain
│   ├── service
│   └── repository
└── IEODPApplication.java
```

## Features

### Authentication & Authorization

- **JWT-based authentication** with access and refresh tokens
- **Token expiration handling** with automatic refresh
- **Four roles**: OPERATIONS, MANAGER, LEADERSHIP, AUDITOR
- **Permission-based access control** at method level using `@PreAuthorize`
- **Secure password encoding** using BCrypt

### Workflow Engine

State-driven workflow with the following states:
- **CREATED** → **REVIEWED** → **APPROVED** / **REJECTED** → **REOPENED**

**Business Rules:**
- Role-based transition validation
- State machine logic enforcement
- Full audit trail for each transition
- Business rule validation per state

### Audit Logging

- **Comprehensive audit trail** for all significant actions
- **Correlation IDs** for request tracing
- **Entity-level tracking** with before/after values
- **IP address and user agent** logging
- **Searchable audit logs** with filtering

### Data Handling

- **Server-side pagination** with Spring Data
- **Sorting and filtering** support
- **Optimized JPA queries** with JOIN FETCH to avoid N+1
- **Database indexing** strategy for performance
- **Transaction management** with proper rollback

### Error Handling

- **Global exception handler** for consistent error responses
- **Standardized error format** with error codes
- **Validation error reporting** with field-level details
- **Proper HTTP status codes**

## API Documentation

Once the application is running, access Swagger UI at:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+ or PostgreSQL 12+

### Database Setup

1. Create database:
```sql
CREATE DATABASE ieodp_db1;
```

2. Update `application.yaml` with your database credentials:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ieodp_db1
    username: your_username
    password: your_password
```

### Running the Application

1. Build the project:
```bash
mvn clean install
```

2. Run the application:
```bash
mvn spring-boot:run
```

3. The application will start on `http://localhost:8080`

### Default Credentials

On first startup, a default admin user is created:
- **Username**: `admin`
- **Password**: `admin123`
- **Role**: `LEADERSHIP`

## API Endpoints

### Authentication (`/api/v1/auth`)

- `POST /api/v1/auth/register` - Register new user
- `POST /api/v1/auth/login` - User login
- `POST /api/v1/auth/refresh` - Refresh access token
- `POST /api/v1/auth/logout` - Logout (revoke refresh token)

### Users (`/api/v1/users`)

- `GET /api/v1/users` - Get all users (paginated)
- `GET /api/v1/users/{id}` - Get user by ID
- `GET /api/v1/users/search` - Search users
- `GET /api/v1/users/role/{roleName}` - Get users by role
- `PUT /api/v1/users/{id}` - Update user
- `DELETE /api/v1/users/{id}` - Delete user

### Workflows (`/api/v1/workflows`)

- `POST /api/v1/workflows` - Create workflow
- `GET /api/v1/workflows/{id}` - Get workflow by ID
- `GET /api/v1/workflows` - Get all workflows (paginated)
- `GET /api/v1/workflows/search` - Search workflows
- `PUT /api/v1/workflows/{id}` - Update workflow
- `POST /api/v1/workflows/{id}/transition` - Transition workflow state
- `DELETE /api/v1/workflows/{id}` - Delete workflow

### Audit (`/api/v1/audit`)

- `GET /api/v1/audit` - Get audit logs (paginated, filtered)
- `GET /api/v1/audit/entity/{entityType}/{entityId}` - Get audit logs for entity

## Security Model

### Roles

1. **OPERATIONS**: Can create and update workflows
2. **MANAGER**: Can review, approve, and reject workflows
3. **LEADERSHIP**: Full access to all features
4. **AUDITOR**: Read-only access to audit logs

### Permissions

Permissions are assigned to roles, not directly to users. Key permissions include:
- `WORKFLOW_CREATE`, `WORKFLOW_READ`, `WORKFLOW_UPDATE`, `WORKFLOW_DELETE`
- `WORKFLOW_REVIEW`, `WORKFLOW_APPROVE`, `WORKFLOW_REJECT`, `WORKFLOW_REOPEN`
- `USER_CREATE`, `USER_READ`, `USER_UPDATE`, `USER_DELETE`
- `AUDIT_READ`, `AUDIT_EXPORT`

## Workflow State Machine

### State Transitions

```
CREATED --[SUBMIT/REVIEW]--> REVIEWED
REVIEWED --[APPROVE]--> APPROVED
REVIEWED --[REJECT]--> REJECTED
APPROVED --[REJECT]--> REJECTED (Leadership only)
REJECTED --[REOPEN]--> REOPENED
REOPENED --[SUBMIT]--> CREATED
```

### Role-Based Transitions

- **CREATED → REVIEWED**: OPERATIONS, MANAGER
- **REVIEWED → APPROVED**: MANAGER, LEADERSHIP
- **REVIEWED → REJECTED**: MANAGER, LEADERSHIP
- **APPROVED → REJECTED**: LEADERSHIP only
- **REJECTED → REOPENED**: OPERATIONS, MANAGER
- **REOPENED → CREATED**: OPERATIONS

## Transaction Management

Transactions are used strategically:

- **Service methods** marked with `@Transactional` for business operations
- **Read-only transactions** for queries (`@Transactional(readOnly = true)`)
- **Rollback on exceptions** - all exceptions trigger rollback
- **Audit logging** is transactional to ensure logs are never lost

## Logging Strategy

- **Structured logging** with correlation IDs
- **Log levels**: DEBUG for development, INFO for production
- **Correlation IDs** enable request tracing across services
- **File logging** with rotation (10MB, 30 days retention)

## Testing

### Test Coverage Strategy

- **Unit tests** for services and business logic
- **Repository tests** for data access layer
- **Controller tests** for API endpoints
- **Integration tests** for end-to-end workflows

Run tests:
```bash
mvn test
```

## Database Indexing Strategy

Indexes are defined on:
- User: `username`, `email`
- WorkflowItem: `state`, `created_by_id`, `assigned_to_id`, `created_at`
- AuditLog: `action`, `performed_by_id`, `entity_type/entity_id`, `created_at`, `correlation_id`
- RefreshToken: `token`, `user_id`, `expiry_date`

## Production Considerations

1. **Security**:
   - Change JWT secret in production
   - Use strong passwords
   - Enable HTTPS
   - Configure CORS properly

2. **Database**:
   - Use connection pooling (HikariCP configured)
   - Set up database backups
   - Monitor query performance

3. **Logging**:
   - Configure log aggregation (ELK, Splunk, etc.)
   - Set appropriate log levels for production
   - Monitor correlation IDs

4. **Performance**:
   - Enable JPA query caching where appropriate
   - Monitor N+1 query issues
   - Use database connection pooling

## Integration Readiness

The API is designed for integration with:
- **Frontend applications** (React, Angular, Vue)
- **Python/AI services** via REST API
- **Power BI/Analytics** via REST API
- **Microservices** via versioned APIs (`/api/v1/`)

### Backward Compatibility

- API versioning strategy: `/api/v1/`
- New versions can be added as `/api/v2/` without breaking existing clients
- DTOs are versioned and backward compatible

## License

Proprietary - All rights reserved

## Support

For issues and questions, contact: support@ieodp.com
