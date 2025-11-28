# 🐳 E-Ticaret Backend - Docker Setup

Enterprise-level E-Commerce Backend Application with Docker containerization.

## 📋 Prerequisites

- Docker Desktop installed
- Docker Compose V2
- 4GB+ available RAM
- Ports available: 5432 (PostgreSQL), 6379 (Redis), 8080 (Backend)

## 🚀 Quick Start

### 1. Start All Services

```bash
# From project root directory
docker-compose up -d
```

This will start:
- ✅ PostgreSQL database
- ✅ Redis cache
- ✅ Spring Boot backend

### 2. Check Status

```bash
docker-compose ps
```

### 3. View Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f backend
docker-compose logs -f postgres
docker-compose logs -f redis
```

### 4. Stop Services

```bash
docker-compose down
```

### 5. Stop and Remove Volumes (⚠️ Deletes all data)

```bash
docker-compose down -v
```

## 🔧 Development Workflow

### Rebuild Backend After Code Changes

```bash
# Rebuild and restart backend only
docker-compose up -d --build backend
```

### Access Services

| Service | URL | Credentials |
|---------|-----|-------------|
| **Backend API** | http://localhost:8080 | - |
| **Health Check** | http://localhost:8080/actuator/health | - |
| **PostgreSQL** | localhost:5432 | postgres/12345 |
| **Redis** | localhost:6379 | No password |

### Database Connection

```properties
Host: localhost
Port: 5432
Database: eticaret
Username: postgres
Password: 12345
```

## 📊 Health Checks

All services have health checks configured:

```bash
# Check backend health
curl http://localhost:8080/actuator/health

# Check PostgreSQL
docker exec eticaret-postgres pg_isready -U postgres

# Check Redis
docker exec eticaret-redis redis-cli ping
```

## 🛠️ Troubleshooting

### Port Already in Use

If PostgreSQL port is already occupied by your existing container:

```bash
# Stop existing container
docker stop eticaret-postgres

# Remove existing container
docker rm eticaret-postgres

# Start new setup
docker-compose up -d
```

### Backend Won't Start

```bash
# Check logs
docker-compose logs backend

# Common issues:
# 1. Database not ready -> Wait for health check
# 2. Port 8080 busy -> Change port in docker-compose.yml
# 3. Build failed -> Check Java 21 is used
```

### Database Connection Failed

```bash
# Verify PostgreSQL is running
docker-compose ps postgres

# Check if database exists
docker exec -it eticaret-postgres psql -U postgres -l
```

### Clean Restart

```bash
# Stop everything
docker-compose down -v

# Remove all images
docker-compose down --rmi all

# Start fresh
docker-compose up --build -d
```

## 🔐 Environment Variables

Create `.env` file in project root for custom configuration:

```env
# Database
DB_USERNAME=postgres
DB_PASSWORD=your_secure_password

# JWT
JWT_SECRET=your_super_secret_key_at_least_256_bits
JWT_EXPIRATION=86400000

# Redis
REDIS_HOST=redis
REDIS_PORT=6379
```

Then reference in docker-compose.yml:

```yaml
backend:
  environment:
    SPRING_DATASOURCE_PASSWORD: ${DB_PASSWORD}
    JWT_SECRET: ${JWT_SECRET}
```

## 📦 Docker Images Used

| Service | Image | Size |
|---------|-------|------|
| PostgreSQL | postgres:16-alpine | ~80MB |
| Redis | redis:7-alpine | ~30MB |
| Backend | Custom (multi-stage) | ~200MB |

## 🎯 Production Deployment

For production, update:

1. **Security**: Change all default passwords
2. **Environment**: Set `SPRING_PROFILES_ACTIVE=prod`
3. **Secrets**: Use Docker secrets or environment variables
4. **Volumes**: Use named volumes for data persistence
5. **Networking**: Configure firewall and expose only necessary ports
6. **Monitoring**: Enable Actuator endpoints with authentication

## 📝 Common Commands

```bash
# Start services in background
docker-compose up -d

# View real-time logs
docker-compose logs -f

# Stop services
docker-compose stop

# Remove everything (including volumes)
docker-compose down -v

# Rebuild specific service
docker-compose build backend

# Scale service (if stateless)
docker-compose up -d --scale backend=3

# Execute command in container
docker-compose exec backend sh
docker-compose exec postgres psql -U postgres eticaret

# Check resource usage
docker stats
```

## 🔄 CI/CD Integration

Example GitHub Actions workflow:

```yaml
steps:
  - name: Build and test
    run: |
      docker-compose -f docker-compose.yml build
      docker-compose up -d
      docker-compose exec -T backend mvn test
```

## 📖 Additional Resources

- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Spring Boot Docker Guide](https://spring.io/guides/topicals/spring-boot-docker/)
- [PostgreSQL Docker Hub](https://hub.docker.com/_/postgres)
- [Redis Docker Hub](https://hub.docker.com/_/redis)

## 🤝 Support

For issues or questions:
1. Check logs: `docker-compose logs`
2. Verify health: `docker-compose ps`
3. Review this README

---

**Last Updated**: 2025-11-28  
**Version**: 1.0.0  
**Author**: E-Ticaret Development Team
