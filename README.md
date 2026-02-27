# LinkLite - Complete Application Guide

A modern URL Shortener with Analytics System built with Spring Boot (Java) and React + TypeScript.

## 🚀 Project Overview

LinkLite is a full-stack web application that allows users to:
- Shorten long URLs into memorable short codes
- Track analytics for shortened URLs (clicks, timestamps, IP addresses)
- View detailed analytics with charts and pagination
- Export click history as CSV
- Manage all shortened URLs from a dashboard

## Technology Stack

### Backend
- **Framework**: Spring Boot 4.0.3 (Java 17)
- **Database**: PostgreSQL / MySQL (JPA/Hibernate)
- **API Documentation**: SpringDoc OpenAPI (Swagger UI)
- **Testing**: JUnit 5, Mockito
- **Security**: Input validation, IP anonymization

### Frontend
- **Framework**: React 18 + TypeScript
- **Build Tool**: Vite
- **Styling**: Tailwind CSS
- **HTTP Client**: Axios
- **Charts**: Recharts
- **UI Icons**: Lucide React

### DevOps
- **Containerization**: Docker & Docker Compose
- **Build Tool**: Maven (backend), npm (frontend)

## 📋 Project Structure

```
LinkLite/
├── backend/linklite/
│   ├── src/
│   │   ├── main/java/com/hcl/linklite/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── entity/
│   │   │   ├── dto/
│   │   │   ├── exception/
│   │   │   ├── util/
│   │   │   └── config/
│   │   ├── resources/
│   │   │   ├── application.yaml
│   │   │   ├── schema.sql
│   │   │   └── verify-schema.sql
│   │   └── test/
│   ├── pom.xml
│   └── Dockerfile
├── frontend/linklite-frontend/
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   ├── services/
│   │   ├── types/
│   │   └── App.tsx
│   ├── package.json
│   ├── vite.config.ts
│   └── tailwind.config.js
└── docker-compose.yml
```

## 🔧 Installation & Setup

### Prerequisites
- Java 17+
- Node.js 18+
- npm or yarn
- PostgreSQL 15+ OR MySQL 8+
- Docker (optional)

### Backend Setup

```bash
cd backend/linklite

# Install dependencies (Maven)
mvn clean install

# Configure database in application.yaml
# Update spring.datasource.url, username, password

# Run database migrations (automatic via Hibernate)
mvn spring-boot:run

# API will be available at http://localhost:8081
```

### Frontend Setup

```bash
cd frontend/linklite-frontend

# Install dependencies
npm install

# Configure API endpoint in .env.local
# VITE_API_URL=http://localhost:8081

# Start development server
npm run dev

# Frontend will be available at http://localhost:5173
```

### Docker Setup (Optional)

```bash
# Build and run with Docker Compose
docker-compose up -d

# Services:
# - Backend: http://localhost:8081
# - Frontend: http://localhost:80
# - Database: PostgreSQL on port 5432
```

## API Endpoints

### URL Management

#### Shorten URL
```http
POST /urls/shorten
Content-Type: application/json

{
  "longUrl": "https://example.com/very/long/path"
}

Response:
{
  "shortCode": "abc123",
  "shortUrl": "http://localhost:8081/abc123",
  "longUrl": "https://example.com/very/long/path",
  "createdAt": "2026-02-27T10:30:00"
}
```

### Analytics Endpoints

#### Get Total Analytics
```http
GET /urls/{shortCode}/analytics
```

#### Get 24-Hour Analytics
```http
GET /urls/{shortCode}/analytics?range=24h
```

#### Get Click History (Paginated)
```http
GET /urls/{shortCode}/analytics/history?page=0&size=10
```

#### Redirect & Track
```http
GET /{shortCode}
# Auto-redirects to original URL and logs click
```

## 🔐 Security Features

- **Input Validation**: Only allow http:// and https:// URLs
- **IP Anonymization**: Last octet of IPv4 addresses anonymized to ".0"
- **SQL Injection Prevention**: JPA parameterized queries
- **XSS Prevention**: React's built-in escaping
- **CSRF Protection**: Spring Security best practices
- **Rate Limiting**: Configurable via application.yaml

## 📊 Key Features

### URL Shortening
- Generate 6-character short codes by default (configurable)
- Retry logic with max 5 attempts (configurable)
- Unique constraint on short codes

### Analytics
- Real-time click tracking
- Anonymized IP addresses
- Click timestamp history
- Pagination support for large datasets
- Time-range filtering (24h, 7d, 30d, all-time)

### Dashboard
- View all shortened URLs
- Sort by creation date or total clicks
- Display analytics cards with click counts
- One-click URL copy to clipboard
- Delete URLs
- Export click history as CSV

## 🧪 Testing

### Backend Tests

```bash
cd backend/linklite

# Run all tests
mvn test

# Run with coverage report
mvn test jacoco:report

# View coverage at target/site/jacoco/index.html
```

Test Classes:
- `UrlServiceTest` - URL creation and retrieval
- `ClickLoggingServiceTest` - Click logging and IP anonymization
- `UrlAnalyticsServiceTest` - Analytics aggregation
- `UrlControllerTest` - API endpoint validation
- `UrlAnalyticsControllerTest` - Analytics endpoint validation
- `UrlValidatorTest` - URL validation logic

### Frontend Tests

```bash
cd frontend/linklite-frontend

# Run tests
npm test

# With coverage
npm test -- --coverage
```

## 📝 API Documentation

- **Swagger UI**: http://localhost:8081/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8081/v3/api-docs
- **OpenAPI YAML**: http://localhost:8081/v3/api-docs.yaml

## 🔄 Build & Deployment

### Build Backend

```bash
cd backend/linklite

# Clean build
mvn clean package

# JAR will be at target/linklite-0.0.1-SNAPSHOT.jar

# Run JAR
java -jar target/linklite-0.0.1-SNAPSHOT.jar
```

### Build Frontend

```bash
cd frontend/linklite-frontend

# Production build
npm run build

# Static files will be at dist/

# Preview production build
npm run preview
```

### Environment Configuration

#### Backend (application.yaml)
```yaml
spring:
  application:
    name: linklite
  datasource:
    url: jdbc:postgresql://localhost:5432/linklite
    username: postgres
    password: password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false

linklite:
  short-code:
    base-url: http://localhost:8081
    length: 6
    max-retries: 5
```

#### Frontend (.env.local)
```
VITE_API_URL=http://localhost:8081
```

## 📌 Configuration

### Database

Supports PostgreSQL and MySQL. Configure in `application.yaml`:

```yaml
# PostgreSQL
spring.datasource.url: jdbc:postgresql://localhost:5432/linklite
spring.datasource.driver-class-name: org.postgresql.Driver

# MySQL
spring.datasource.url: jdbc:mysql://localhost:3306/linklite
spring.datasource.driver-class-name: com.mysql.cj.jdbc.Driver
```

### Short Code Generation

```yaml
linklite:
  short-code:
    length: 6              # Default 6 characters
    max-retries: 5         # Retry attempts for uniqueness
    base-url: http://localhost:8081
```

## 🐛 Troubleshooting

### Database Connection Issues
- Ensure PostgreSQL/MySQL is running
- Check credentials in application.yaml
- Verify database exists: `CREATE DATABASE linklite;`

### Frontend API Connection Issues
- Ensure backend is running on port 8081
- Check VITE_API_URL environment variable
- Check browser console for CORS errors

### Port Already in Use
```bash
# Kill process on port 8081
lsof -ti:8081 | xargs kill -9

# Kill process on port 5173
lsof -ti:5173 | xargs kill -9
```

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [React Documentation](https://react.dev)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)
- [Tailwind CSS](https://tailwindcss.com)
- [Swagger/OpenAPI](https://swagger.io)

## ✅ Development Checklist

- [x] Backend services completed
- [x] IP anonymization implemented
- [x] Exception handling configured
- [x] Atomic click increment queries added
- [x] Unit and integration tests created
- [x] Frontend components completed
- [x] API service with retry logic
- [x] Environment configuration
- [x] Swagger/SpringDoc OpenAPI
- [x] Docker containerization
- [x] Documentation

## 📄 License

This project is licensed under the Apache License 2.0 - see LICENSE file for details.

## 👥 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/improvement`)
3. Commit your changes (`git commit -am 'Add improvement'`)
4. Push to the branch (`git push origin feature/improvement`)
5. Create a Pull Request

## 📞 Support

For support and questions, please create an issue in the repository or contact support@linklite.com

---

**Last Updated**: February 27, 2026
**Version**: 1.0.0
