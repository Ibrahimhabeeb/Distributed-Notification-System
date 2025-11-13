🚀 1. What This Service Does

Your Template Service is a standalone microservice that:

Stores and manages all notification templates (for email, push, and even SMS).

Supports multiple versions of a single template (for updates or improvements).

Allows rendering (filling variables like {{name}}) dynamically for use by Email/Push services.

Provides admin endpoints for managing templates.

Provides public endpoints for other services to fetch and render templates.

Provides health checks for monitoring and CI/CD.

Essentially, it’s like a “template warehouse” for your whole system.

🧱 2. Database Structure

You’ll have two main tables:

🗂️ templates

Stores base metadata about each template.

Column	Type	Description
id	uuid (PK)	Unique template ID
template_key	varchar(100)	Unique identifier e.g. "welcome_email"
name	varchar(150)	Human-readable name
description	text	Optional description
language	varchar(5)	e.g. "en", "fr", "yo"
subject	varchar(255)	Optional (for email)
body	text	Main message body with placeholders
variables	jsonb	Extracted variables, e.g. ["name", "otp"]
category	varchar(50)	"email", "push", "sms"
created_at	timestamptz	Default now()
updated_at	timestamptz	Default now()

🧩 Each “template_key” can have multiple versions, tracked in another table (e.g. template_versions).

🧾 3. Endpoints Breakdown

Here’s a friendly, developer-style breakdown of every endpoint you need to build 👇

🟢 PUBLIC ENDPOINTS (for Email & Push Services)
1️⃣ GET /templates/get_latest

Used by other microservices to fetch the latest version of a template.

Query Params

Param	Type	Required	Default
template_key	string	✅ Yes	—
language	string	❌ No	"en"

Response:

{
  "success": true,
  "data": {
    "template_key": "welcome_email",
    "language": "en",
    "version_number": 3,
    "subject": "Welcome {{name}}",
    "body": "Hello {{name}}, welcome to SefrelShop!",
    "variables": ["name"]
  },
  "message": "Fetched latest template successfully"
}

2️⃣ POST /templates/render

Used to substitute actual values into a template.

Request:

{
  "template_key": "welcome_email",
  "language": "en",
  "variables": {
    "name": "Habeeb",
    "otp": "9933"
  }
}


Response:

{
  "success": true,
  "data": {
    "subject": "Welcome Habeeb",
    "body": "Hello Habeeb, your OTP is 9933"
  },
  "message": "Render successful"
}


You can use a rendering engine like:

Python: jinja2

Node.js: mustache or handlebars

🔒 ADMIN / INTERNAL ENDPOINTS
3️⃣ POST /templates

Create a new template base record.

{
  "template_key": "welcome_email",
  "category": "email",
  "description": "Welcome message to new users",
  "created_by": "user_uuid"
}


This creates a template shell without body content yet.

4️⃣ POST /templates/add_version

Add a new version to an existing template.

{
  "template_id": "uuid",
  "language": "en",
  "subject": "Hello {{name}}",
  "body": "Hi {{name}}, welcome!",
  "variables": ["name"],
  "created_by": "user_uuid"
}


Each new version should increment version_number.

5️⃣ GET /templates

List all templates with filters + pagination.

Query:

GET /templates?category=email&page=1&limit=20


Response:

{
  "success": true,
  "data": [
    {
      "template_key": "welcome_email",
      "category": "email",
      "latest_version": 3,
      "created_at": "2025-11-10T12:00:00Z"
    }
  ],
  "meta": {
    "total": 1,
    "limit": 20,
    "page": 1,
    "total_pages": 1,
    "has_next": false,
    "has_previous": false
  }
}

6️⃣ GET /templates/{template_id}

Fetch details of one template + versions.

7️⃣ GET /templates/{template_id}/versions

List all versions for a given template.

8️⃣ DELETE /templates/{template_id}

Soft delete a template (set a flag like is_deleted = true).

9️⃣ DELETE /templates/version/{version_id}

Delete a specific version.

🔟 PUT /templates/version/{version_id}

Update an existing version.

💓 HEALTH CHECK & INTERNAL OPS
GET /health

Used by DevOps, CI/CD, and monitoring tools.

Response:

{
  "status": "healthy",
  "uptime": "5h 12m",
  "db": "connected",
  "redis": "connected"
}

💾 4. Core Functionalities
Functionality	Description
Versioning	Track multiple template versions via version numbers
Variable Extraction	Parse {{variable}} placeholders into the variables array
Rendering	Replace variables with runtime data
Caching (Redis)	Store frequently used templates for faster access
Validation	Ensure variable keys in variables field match those used in the body
Soft Delete	Don’t permanently remove templates; mark inactive
🔁 5. How It Fits in the Ecosystem
Service	Role
API Gateway	Accepts notification request and passes template_key
Template Service	Provides subject & body via render endpoint
Email / Push Service	Uses rendered output to send messages
User Service	Provides user contact and language preferences

Example Flow

API Gateway receives /send_notification.

It checks user preferences from the User Service.

Fetches the template from Template Service → /render.

Sends the rendered message to Email or Push Service.

The queue manages async delivery.

🧩 6. Suggested Folder Structure (FastAPI Example)
template_service/
├── app/
│   ├── main.py
│   ├── api/
│   │   ├── routes/
│   │   │   ├── templates.py
│   │   │   ├── versions.py
│   │   │   ├── health.py
│   │   └── dependencies.py
│   ├── db/
│   │   ├── models.py
│   │   ├── database.py
│   └── core/
│       ├── config.py
│       ├── utils.py
│       ├── render_engine.py
├── Dockerfile
├── requirements.txt
└── README.md

🧰 7. Libraries You’ll Likely Need (if Python/FastAPI)
fastapi
uvicorn
sqlalchemy
psycopg2-binary
jinja2
pydantic
redis
python-dotenv