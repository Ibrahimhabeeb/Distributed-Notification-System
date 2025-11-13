# Template Service

A FastAPI microservice for managing notification templates with versioning support, dynamic rendering, and comprehensive API endpoints.

## Features

- **Template Management** - Create, read, update, and delete notification templates
- **Version Control** - Multiple versions per template with automatic versioning
- **Dynamic Rendering** - Variable substitution using Jinja2 template engine
- **Multi-language Support** - Templates can be created in different languages
- **Pagination** - Efficient listing of templates with pagination support
- **Health Monitoring** - Database and Redis connection status monitoring
- **Soft Deletes** - Safe deletion without losing historical data
- **Interactive API Docs** - Auto-generated Swagger UI documentation

## Requirements

- Python 3.8+
- SQLite (for local development) or PostgreSQL (for production)
- Redis (optional, for caching)

## Installation

### 1. Clone and Setup
```bash
cd template_service
pip install -r requirements.txt
```

### 2. Environment Configuration
Create a `.env` file in the root directory:
```bash
DATABASE_URL=sqlite:///./templates.db
REDIS_URL=redis://localhost:6379/0
```

For PostgreSQL:
```bash
DATABASE_URL=postgresql://username:password@localhost:5432/templates_db
REDIS_URL=redis://localhost:6379/0
```

## Running the Service

### Method 1: Direct Python Execution
```bash
# Set environment variables and run
DATABASE_URL=sqlite:///./templates.db REDIS_URL=redis://localhost:6379/0 python3 app/main.py
```

### Method 2: Using Uvicorn
```bash
# From the template_service directory
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

### Method 3: Using Docker
```bash
# Build the Docker image
docker build -t template-service .

# Run the container
docker run -p 8000:8000 \
  -e DATABASE_URL=sqlite:///./templates.db \
  -e REDIS_URL=redis://localhost:6379/0 \
  template-service
```

## API Documentation

Once the service is running, access the interactive API documentation at:
- **Swagger UI**: http://localhost:8000/docs
- **ReDoc**: http://localhost:8000/redoc

## API Endpoints

### Public Endpoints (for other microservices)
- `GET /templates/get_latest` - Get the latest version of a template
- `POST /templates/render` - Render template with variables

### Admin Endpoints
- `POST /templates/` - Create a new template
- `GET /templates/` - List all templates (with pagination)
- `GET /templates/{template_id}` - Get specific template details
- `DELETE /templates/{template_id}` - Soft delete a template
- `POST /templates/add_version` - Add a new version to existing template
- `GET /templates/{template_id}/versions` - List all versions of a template
- `PUT /templates/version/{version_id}` - Update a template version
- `DELETE /templates/version/{version_id}` - Delete a template version

### Health Check
- `GET /health/` - Service health status

## Usage Examples

### Create a Template
```bash
curl -X POST "http://localhost:8000/templates/" \
  -H "Content-Type: application/json" \
  -d '{
    "template_key": "welcome_email",
    "name": "Welcome Email",
    "category": "email",
    "description": "Welcome email for new users"
  }'
```

### Add a Template Version
```bash
curl -X POST "http://localhost:8000/templates/add_version" \
  -H "Content-Type: application/json" \
  -d '{
    "template_id": "your-template-id",
    "language": "en",
    "subject": "Welcome {{name}}!",
    "body": "Hello {{name}}, welcome to our platform!",
    "variables": ["name"]
  }'
```

### Render a Template
```bash
curl -X POST "http://localhost:8000/templates/render" \
  -H "Content-Type: application/json" \
  -d '{
    "template_key": "welcome_email",
    "language": "en",
    "variables": {
      "name": "John Doe"
    }
  }'
```

### Get Latest Template
```bash
curl "http://localhost:8000/templates/get_latest?template_key=welcome_email&language=en"
```

## Project Structure

```
template_service/
├── app/
│   ├── main.py              # FastAPI application entry point
│   ├── api/
│   │   ├── routes/
│   │   │   ├── templates.py # Template management endpoints
│   │   │   ├── versions.py  # Version management endpoints
│   │   │   └── health.py    # Health check endpoint
│   │   └── dependencies.py  # API dependencies
│   ├── db/
│   │   ├── models.py        # SQLAlchemy database models
│   │   └── database.py      # Database connection setup
│   └── core/
│       ├── config.py        # Configuration management
│       ├── schemas.py       # Pydantic models for validation
│       ├── render_engine.py # Jinja2 template rendering
│       └── utils.py         # Utility functions
├── Dockerfile              # Docker container configuration
├── requirements.txt        # Python dependencies
├── .env                    # Environment variables (create this)
└── README.md              # This file
```

## Testing

### Health Check
```bash
curl http://localhost:8000/health/
```

### List Templates
```bash
curl http://localhost:8000/templates/
```

## Environment Variables

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `DATABASE_URL` | Database connection string | - | Yes |
| `REDIS_URL` | Redis connection string | - | Yes |

## Docker Support

The service includes a Dockerfile for containerization. The container exposes port 8000 and expects environment variables to be passed at runtime.

## Technologies Used

- **FastAPI** - Modern, fast web framework for building APIs
- **SQLAlchemy** - SQL toolkit and ORM
- **SQLite/PostgreSQL** - Database engines
- **Jinja2** - Template rendering engine
- **Pydantic** - Data validation using Python type annotations
- **Uvicorn** - ASGI server for running FastAPI applications

## Integration

This service is designed to work as part of a larger notification system:

1. **API Gateway** receives notification requests
2. **Template Service** provides rendered templates
3. **Email/Push Services** send the notifications
4. **User Service** provides user preferences and contact information

## Security Notes

- This service should be deployed behind an API Gateway
- Admin endpoints should be secured with proper authentication
- Public endpoints are designed for inter-service communication
- Consider implementing rate limiting for public endpoints

## License

This project is part of the HNG-13 backend template system.
