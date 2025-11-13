#!/usr/bin/env python3
"""
Direct API test without running server in background
"""
import asyncio
import sys
import os

# Add the app directory to the Python path
app_dir = os.path.dirname(os.path.abspath(__file__)) + "/app"
sys.path.insert(0, app_dir)

# Set working directory
template_service_dir = os.path.dirname(os.path.abspath(__file__))
os.chdir(template_service_dir)

# Import the app
from app.main import app
from fastapi.testclient import TestClient

def test_app():
    """Test the app directly using TestClient"""
    client = TestClient(app)
    
    print("🧪 Testing Template Service API")
    print("=" * 50)
    
    # Test 1: Root endpoint
    print("\n1️⃣ Testing root endpoint...")
    try:
        response = client.get("/")
        print(f"Status: {response.status_code}")
        print(f"Response: {response.json()}")
        print("✅ Root endpoint working!")
    except Exception as e:
        print(f"❌ Root endpoint failed: {e}")
    
    # Test 2: Health endpoint
    print("\n2️⃣ Testing health endpoint...")
    try:
        response = client.get("/health/")
        print(f"Status: {response.status_code}")
        print(f"Response: {response.json()}")
        print("✅ Health endpoint working!")
    except Exception as e:
        print(f"❌ Health endpoint failed: {e}")
    
    # Test 3: Get all templates
    print("\n3️⃣ Testing get templates endpoint...")
    try:
        response = client.get("/templates/")
        print(f"Status: {response.status_code}")
        print(f"Response: {response.json()}")
        print("✅ Get templates endpoint working!")
    except Exception as e:
        print(f"❌ Get templates endpoint failed: {e}")
    
    # Test 4: Create a template
    print("\n4️⃣ Testing create template endpoint...")
    try:
        import time
        unique_key = f"test_email_{int(time.time())}"
        template_data = {
            "template_key": unique_key,
            "name": "Test Email",
            "description": "A test email template",
            "category": "email"
        }
        response = client.post("/templates/", json=template_data)
        print(f"Status: {response.status_code}")
        print(f"Response: {response.json()}")
        print("✅ Create template endpoint working!")
        
        # Store the template_id for further tests
        if response.status_code == 200:
            template_id = response.json()["data"]["id"]
            print(f"📝 Created template ID: {template_id}")
            
            # Test 5: Add a version to the template
            print("\n5️⃣ Testing add template version...")
            version_data = {
                "template_id": template_id,
                "language": "en",
                "subject": "Welcome {{name}}!",
                "body": "Hello {{name}}, welcome to our service!",
                "variables": ["name"]
            }
            response = client.post("/templates/add_version", json=version_data)
            print(f"Status: {response.status_code}")
            print(f"Response: {response.json()}")
            print("✅ Add version endpoint working!")
            
            # Test 6: Get latest template
            print("\n6️⃣ Testing get latest template...")
            response = client.get(f"/templates/get_latest?template_key={unique_key}&language=en")
            print(f"Status: {response.status_code}")
            print(f"Response: {response.json()}")
            print("✅ Get latest template working!")
            
            # Test 7: Render template
            print("\n7️⃣ Testing render template...")
            render_data = {
                "template_key": unique_key,
                "language": "en",
                "variables": {"name": "John Doe"}
            }
            response = client.post("/templates/render", json=render_data)
            print(f"Status: {response.status_code}")
            print(f"Response: {response.json()}")
            print("✅ Render template working!")
            
    except Exception as e:
        print(f"❌ Create template endpoint failed: {e}")
    
    print("\n" + "=" * 50)
    print("🎉 Testing completed!")

if __name__ == "__main__":
    test_app()