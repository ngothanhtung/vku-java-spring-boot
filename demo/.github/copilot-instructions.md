# VKU Java Spring Boot Demo - AI Coding Agent Instructions

## Project Architecture

This is a comprehensive Spring Boot 3.5.4 demo application built for educational purposes, showcasing modern Java enterprise patterns with Java 21, MySQL, Redis, and WebSocket features.

### Core Technology Stack

- **Framework**: Spring Boot 3.5.4 with Java 21 toolchain
- **Database**: MySQL with JPA/Hibernate (auto-DDL updates enabled)
- **Caching**: Redis with Spring Cache abstraction
- **Security**: JWT-based authentication with role-based authorization
- **Real-time**: WebSocket support for chat/messaging features
- **Build**: Gradle with Lombok for code generation

### Package Structure & Patterns

Follow the established package organization:

```
com.example.demo/
├── config/          # @Configuration classes for framework setup
├── controllers/     # @RestController/@Controller for API endpoints
├── dtos/           # Request/Response DTOs with validation
├── entities/       # JPA entities with Lombok annotations
├── enums/          # Application enums (StudentStatus, etc.)
├── exceptions/     # Custom exceptions and global error handling
├── filters/        # Security filters (JwtAuthenticationFilter)
├── repositories/   # JPA repositories with custom queries
└── services/       # @Service business logic layer
```

## Key Development Patterns

### Entity Design

- Use Lombok `@Getter/@Setter` instead of manual getters/setters
- Apply `@EntityGraph` to prevent N+1 queries: `@EntityGraph(attributePaths = {"department", "courses"})`
- Define named entity graphs in entities for complex loading strategies
- Use `@ManyToMany(fetch = FetchType.EAGER)` with `@JoinTable` for role mappings

### Repository Layer

- Extend `JpaRepository<Entity, Long>` and `JpaSpecificationExecutor<Entity>` for dynamic queries
- Use `@Query` with JOIN FETCH for eager loading: `"SELECT s FROM Student s LEFT JOIN FETCH s.department"`
- Implement projection interfaces like `StudentProjection` for lightweight queries
- Apply `@Modifying(clearAutomatically = true)` for batch updates/deletes

### Service Layer Caching

- Use `@Cacheable(value = "students", key = "#id")` for read operations
- Apply cache keys with dynamic parameters: `key = "'department-' + #departmentId"`
- Implement `@CacheEvict` and `@CachePut` for cache invalidation
- Configure custom cache names in `CacheConfig.java`

### Security Configuration

- JWT stateless authentication with `SessionCreationPolicy.STATELESS`
- Role-based access: `.requestMatchers("/api/students/**").hasAnyRole("Administrators", "Managers")`
- Custom security handlers: `CustomAuthenticationEntryPoint`, `CustomAccessDeniedHandler`
- Filter chain: `JwtAuthenticationFilter` before `UsernamePasswordAuthenticationFilter`

### Async & Scheduling Patterns

- Use `@Async` with named executors: `@Async("emailTaskExecutor")`
- Schedule with fixed delays: `@Scheduled(fixedDelay = 5000)` for queue processing
- Configure custom task executors in `TaskExecutorConfig.java`
- Enable async support with `@EnableAsync` in configuration

## Development Workflows

### Database Operations

- **Schema**: Auto-updates via `spring.jpa.hibernate.ddl-auto=update`
- **Initial Data**: SQL scripts in `src/main/resources/data.sql` run on startup
- **Connections**: Environment-based config with fallback defaults:
  ```properties
  spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:mysql://localhost:3307/spring}
  ```

### Build & Run Commands

```bash
# Development mode with hot reload
./gradlew bootRun

# Build JAR for production
./gradlew bootJar

# Run tests
./gradlew test

# Docker development stack
docker compose -p vku-java-spring-boot-demo up

# Cleanup Docker resources
docker compose -p vku-java-spring-boot-demo down -v
```

### Testing Patterns

- Use `test-resume.sh` script for complex file upload testing
- Integration tests verify Redis queue processing and WebSocket connections
- Test database operations with `@Transactional` rollback

## Component Integration Points

### Redis Queue System

- **Email Queue**: Traditional in-memory queue with `@Scheduled` processing
- **Redis Queue**: Distributed queue using `RedisTemplate<String, Object>`
- **Queue Services**: `EmailQueueService` vs `EmailRedisQueueService` for comparison

### WebSocket Architecture

- **Config**: `WebSocketConfig.java` with STOMP messaging
- **Controllers**: Separate `@Controller` classes for WebSocket vs REST endpoints
- **Frontend**: Static HTML demo at `/static/websocket-demo.html`

### File Upload System

- **Chunked Upload**: Resume-capable uploads with upload ID tracking
- **Storage**: Configurable directory via `app.upload.directory=${java.io.tmpdir}/uploads`
- **Validation**: SHA-256 hash verification for file integrity

### Authentication Flow

- **Login**: JWT generation via `JwtService` with configurable expiration
- **Authorization**: Role-based with eager-loaded user roles from database
- **Google OAuth**: Separate endpoints for credential-based and token-based login

## Error Handling Conventions

Use `GlobalExceptionHandler` with `@ControllerAdvice` for consistent error responses:

- Custom `HttpException` for business logic errors
- Validation errors automatically mapped to structured responses
- Security exceptions handled by custom entry points

## Environment Configuration

Development uses localhost defaults; production requires environment variables:

- `SPRING_DATASOURCE_*` for MySQL connection
- `SPRING_REDIS_*` for Redis configuration
- `app.upload.directory` for file storage location

The application emphasizes practical enterprise patterns over simple CRUD operations, showcasing caching strategies, async processing, security integration, and real-time features typical in production Spring Boot applications.
