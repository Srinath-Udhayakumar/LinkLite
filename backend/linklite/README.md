# LinkLite - URL Shortener Service

A production-ready URL shortener service with analytics, built with Spring Boot and following industry best practices.

## Features

- **URL Shortening**: Convert long URLs to short, memorable codes
- **Redirect Service**: Fast redirects with analytics tracking
- **Comprehensive Analytics**: Track clicks, geographic data, and time-based analytics
- **RESTful API**: Clean, well-documented API endpoints
- **Production Ready**: Docker support, CI/CD pipeline, health monitoring
- **Validations**: Input validation with proper error handling
- **Performance**: Optimized database queries and caching

## Quick Start

### Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL 15+
- Docker (optional)

### Running Locally

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd linklite
   ```

2. **Database Setup**
   ```sql
   CREATE DATABASE linklite_db;
   CREATE USER linklite_user WITH PASSWORD 'your_password';
   GRANT ALL PRIVILEGES ON DATABASE linklite_db TO linklite_user;
   ```

3. **Configuration**
   Update `application.yaml` with your database credentials:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/linklite_db
       username: linklite_user
       password: your_password
   ```

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

The application will be available at `http://localhost:8081/api/v1`

## Docker Deployment

### Build and Run

```bash
# Build the image
docker build -t linklite .

# Run with PostgreSQL
docker-compose up -d
```

### Docker Compose

```yaml
version: '3.8'
services:
  linklite:
    build: .
    ports:
      - "8081:8081"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/linklite_db
      - SPRING_DATASOURCE_USERNAME=linklite_user
      - SPRING_DATASOURCE_PASSWORD=your_password
    depends_on:
      - postgres

  postgres:
    image: postgres:15
    environment:
      - POSTGRES_DB=linklite_db
      - POSTGRES_USER=linklite_user
      - POSTGRES_PASSWORD=your_password
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5432:5432"
```

## API Documentation

### Base URL
```
http://localhost:8081/api/v1
```

### Endpoints

#### URL Shortening

**Create Short URL**
```http
POST /urls/shorten
Content-Type: application/json

{
  "longUrl": "https://www.example.com/very/long/url"
}
```

**Response:**
```json
{
  "shortCode": "abc123",
  "shortUrl": "http://localhost:8081/abc123",
  "longUrl": "https://www.example.com/very/long/url",
  "createdAt": "2024-02-24 12:00:00"
}
```

#### Redirect Service

**Redirect to Original URL**
```http
GET /{shortCode}
```

**Response:** `302 Redirect` to the original URL

#### Analytics

**Get Total Analytics**
```http
GET /urls/{shortCode}/analytics
```

**Response:**
```json
{
  "shortCode": "abc123",
  "longUrl": "https://www.example.com/very/long/url",
  "totalClicks": 42,
  "createdAt": "2024-02-24 12:00:00",
  "recentClicks": [
    {
      "clickedAt": "2024-02-24 13:00:00",
      "ipAddress": "192.168.1.1"
    }
  ]
}
```

**Get 24-Hour Analytics**
```http
GET /urls/{shortCode}/analytics?range=24h
```

**Response:**
```json
{
  "shortCode": "abc123",
  "range": "24h",
  "clickCount": 5
}
```

**Get Click History (Paginated)**
```http
GET /urls/{shortCode}/analytics/history?page=0&size=10
```

**Response:**
```json
{
  "content": [
    {
      "clickedAt": "2024-02-24T13:00:00"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 42
  },
  "totalPages": 5
}
```

### Health Checks

**Application Health**
```http
GET /actuator/health
```

**Response:**
```json
{
  "status": "UP",
  "groups": ["liveness", "readiness"]
}
```

## Error Handling

The API uses standard HTTP status codes:

- `200` - Success
- `201` - Created
- `302` - Redirect
- `400` - Bad Request (validation errors)
- `404` - Not Found
- `500` - Internal Server Error

**Error Response Format:**
```json
{
  "timestamp": "2024-02-24 12:00:00",
  "status": 400,
  "error": "VALIDATION_FAILED",
  "message": "Request validation failed",
  "path": "/api/v1/urls/shorten",
  "validationErrors": {
    "longUrl": "Invalid URL format"
  }
}
```

## Architecture

### Technology Stack

- **Framework**: Spring Boot 4.0.3
- **Database**: PostgreSQL 15 with Hibernate ORM
- **Language**: Java 17
- **Build Tool**: Maven 3.9
- **Containerization**: Docker
- **CI/CD**: GitHub Actions

### Project Structure

```
src/main/java/com/hcl/linklite/
├── LinkliteApplication.java          # Main application class
├── config/                        # Configuration classes
│   ├── GlobalExceptionHandler.java   # Centralized error handling
│   └── WebConfig.java            # Web configuration
├── controller/                    # REST controllers
│   ├── UrlController.java         # URL shortening endpoints
│   ├── RedirectController.java     # Redirect handling
│   └── UrlAnalyticsController.java # Analytics endpoints
├── dto/                          # Data Transfer Objects
│   ├── UrlShortenRequest.java    # Request DTOs
│   ├── UrlShortenResponse.java   # Response DTOs
│   └── AnalyticsResponse.java     # Analytics DTOs
├── entity/                        # JPA entities
│   ├── Url.java                 # URL entity
│   └── UrlClick.java            # Click tracking entity
├── repository/                    # Data access layer
│   ├── UrlRepository.java         # URL repository
│   └── UrlClickRepository.java  # Click repository
├── service/                       # Business logic layer
│   ├── UrlService.java           # Core URL service
│   ├── UrlAnalyticsService.java  # Analytics service
│   └── ShortCodeService.java    # Short code generation
├── exception/                     # Custom exceptions
│   ├── UrlNotFoundException.java
│   └── ResourceNotFoundException.java
└── util/                          # Utility classes
    ├── UrlValidator.java         # URL validation
    └── shortcode/              # Short code utilities
```

## Development

### Running Tests

```bash
# Unit tests
./mvnw test

# Integration tests
./mvnw verify

# Generate test report
./mvnw surefire-report:report
```

### Code Quality

The project follows industry best practices:

- **Clean Architecture**: Layered structure with clear separation of concerns
- **SOLID Principles**: Single responsibility, dependency injection
- **Validation**: Comprehensive input validation with proper error messages
- **Error Handling**: Centralized exception handling with consistent responses
- **Logging**: Structured logging with appropriate levels
- **Security**: Input sanitization and validation
- **Performance**: Optimized queries and connection pooling
- **Monitoring**: Health checks and metrics endpoints

### Environment Profiles

- **dev**: Development with H2 database and detailed logging
- **test**: Testing with H2 in-memory database
- **prod**: Production with PostgreSQL and optimized settings

## Monitoring and Observability

### Health Endpoints

- `/actuator/health` - Application health status
- `/actuator/info` - Application information
- `/actuator/metrics` - Application metrics
- `/actuator/prometheus` - Prometheus metrics

### Logging

Structured logging with configurable levels:
- `com.hcl.linklite` - Application logs
- `org.hibernate.SQL` - Database query logs
- `org.springframework.web` - Web request logs

## Security Considerations

- **Input Validation**: All inputs validated using Jakarta validation
- **SQL Injection Prevention**: Parameterized queries
- **XSS Prevention**: Input sanitization
- **Rate Limiting**: Consider implementing for production
- **HTTPS**: Use HTTPS in production environments

## Performance Optimizations

- **Database Indexing**: Optimized queries with proper indexes
- **Connection Pooling**: HikariCP with tuned parameters
- **Batch Processing**: JDBC batch operations enabled
- **Caching**: Consider Redis for frequently accessed data
- **Lazy Loading**: JPA relationships configured for lazy loading

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -am 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions:
- Create an issue in the repository
- Check the API documentation
- Review the health check endpoints
