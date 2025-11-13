#!/usr/bin/env python3

# Demo script showing Template Service API responses in the new standardized format
import json

def show_response_examples():
    print("Template Service API Response Examples (Standardized Format)")
    print("=" * 65)
    
    print("\n1. ROOT ENDPOINT RESPONSE:")
    print("GET /")
    print(json.dumps({
        "success": True,
        "data": None,
        "error": None,
        "message": "Welcome to the Template Service!",
        "meta": None
    }, indent=2))
    
    print("\n2. HEALTH CHECK RESPONSE:")
    print("GET /health/")
    print(json.dumps({
        "success": True,
        "data": {
            "status": "healthy",
            "uptime": "0.05s",
            "db": "connected",
            "redis": "connected"
        },
        "error": None,
        "message": "Health check completed successfully",
        "meta": None
    }, indent=2))
    
    print("\n3. CREATE TEMPLATE RESPONSE:")
    print("POST /templates/")
    print("Request:")
    print(json.dumps({
        "template_key": "welcome_email",
        "name": "Welcome Email",
        "category": "email",
        "description": "Welcome email for new users"
    }, indent=2))
    print("Response:")
    print(json.dumps({
        "success": True,
        "data": {
            "id": "550e8400-e29b-41d4-a716-446655440000",
            "template_key": "welcome_email",
            "name": "Welcome Email",
            "description": "Welcome email for new users",
            "category": "email",
            "created_at": "2025-11-11T07:00:00Z",
            "latest_version": None,
            "versions": []
        },
        "error": None,
        "message": "Template created successfully",
        "meta": None
    }, indent=2))
    
    print("\n4. ADD TEMPLATE VERSION RESPONSE:")
    print("POST /templates/add_version")
    print("Response:")
    print(json.dumps({
        "success": True,
        "data": {
            "id": "660f9500-f39c-52e5-b827-557766551111",
            "language": "en",
            "subject": "Welcome {{name}}!",
            "body": "Hello {{name}}, welcome to our platform!",
            "variables": ["name"],
            "version_number": 1,
            "created_at": "2025-11-11T07:05:00Z"
        },
        "error": None,
        "message": "Template version added successfully",
        "meta": None
    }, indent=2))
    
    print("\n5. GET LATEST TEMPLATE RESPONSE:")
    print("GET /templates/get_latest?template_key=welcome_email&language=en")
    print(json.dumps({
        "success": True,
        "data": {
            "template_key": "welcome_email",
            "language": "en",
            "version_number": 1,
            "subject": "Welcome {{name}}!",
            "body": "Hello {{name}}, welcome to our platform!",
            "variables": ["name"]
        },
        "error": None,
        "message": "Fetched latest template successfully",
        "meta": None
    }, indent=2))
    
    print("\n6. RENDER TEMPLATE RESPONSE:")
    print("POST /templates/render")
    print("Response:")
    print(json.dumps({
        "success": True,
        "data": {
            "subject": "Welcome John Doe!",
            "body": "Hello John Doe, welcome to our platform!"
        },
        "error": None,
        "message": "Render successful",
        "meta": None
    }, indent=2))
    
    print("\n7. LIST TEMPLATES RESPONSE (with pagination):")
    print("GET /templates/?page=1&limit=20")
    print(json.dumps({
        "success": True,
        "data": [
            {
                "template_key": "welcome_email",
                "category": "email",
                "latest_version": 1,
                "created_at": "2025-11-11T07:00:00Z"
            },
            {
                "template_key": "password_reset",
                "category": "email", 
                "latest_version": 2,
                "created_at": "2025-11-11T08:00:00Z"
            }
        ],
        "error": None,
        "message": "Templates retrieved successfully",
        "meta": {
            "total": 2,
            "limit": 20,
            "page": 1,
            "total_pages": 1,
            "has_next": False,
            "has_previous": False
        }
    }, indent=2))
    
    print("\n8. ERROR RESPONSE EXAMPLE:")
    print("GET /templates/get_latest?template_key=nonexistent")
    print(json.dumps({
        "success": False,
        "data": None,
        "error": "Template not found",
        "message": "Template not found",
        "meta": None
    }, indent=2))
    
    print("\n" + "=" * 65)
    print("✅ ALL RESPONSES NOW FOLLOW THE STANDARD FORMAT:")
    print("   - success: boolean")
    print("   - data: T | null")  
    print("   - error: string | null")
    print("   - message: string")
    print("   - meta: PaginationMeta | null")

if __name__ == "__main__":
    show_response_examples()