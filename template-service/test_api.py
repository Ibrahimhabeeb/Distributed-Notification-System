#!/usr/bin/env python3
"""
Quick API test script to verify endpoints are working
"""
import requests
import json

BASE_URL = "http://localhost:8001"

def test_endpoint(method, endpoint, data=None, expected_status=200):
    """Test a specific endpoint"""
    url = f"{BASE_URL}{endpoint}"
    
    try:
        if method.upper() == "GET":
            response = requests.get(url)
        elif method.upper() == "POST":
            response = requests.post(url, json=data)
        elif method.upper() == "PUT":
            response = requests.put(url, json=data)
        elif method.upper() == "DELETE":
            response = requests.delete(url)
        else:
            print(f"Unsupported method: {method}")
            return False
            
        print(f"{method} {endpoint}")
        print(f"Status Code: {response.status_code}")
        print(f"Response: {response.text}")
        print("-" * 50)
        
        return response.status_code == expected_status
        
    except requests.exceptions.ConnectionError:
        print(f"❌ Connection error for {method} {endpoint}")
        return False
    except Exception as e:
        print(f"❌ Error testing {method} {endpoint}: {e}")
        return False

def main():
    print("Testing Template Service API...")
    print("=" * 50)
    
    # Test basic endpoints
    tests = [
        ("GET", "/", None, 200),
        ("GET", "/health", None, 200),
        ("GET", "/templates", None, 200),
        ("GET", "/templates/1/versions", None, 404),  # Expected 404 since template 1 might not exist
    ]
    
    for method, endpoint, data, expected_status in tests:
        test_endpoint(method, endpoint, data, expected_status)
    
    # Test creating a template
    print("Testing template creation...")
    template_data = {
        "name": "Test Template",
        "description": "A test template for verification",
        "subject": "Test Subject",
        "body": "Hello {{name}}, this is a test template!",
        "template_type": "email"
    }
    
    test_endpoint("POST", "/templates", template_data, 201)
    
    # Test getting all templates after creation
    print("Getting all templates after creation...")
    test_endpoint("GET", "/templates", None, 200)

if __name__ == "__main__":
    main()