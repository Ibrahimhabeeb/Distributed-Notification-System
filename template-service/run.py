#!/usr/bin/env python3
"""
Template Service Runner

This script ensures the application runs from the correct directory
and handles environment setup properly, regardless of where it's called from.
"""

import os
import sys
import subprocess
import argparse

def main():
    # Parse command line arguments
    parser = argparse.ArgumentParser(description="Template Service Runner")
    parser.add_argument("--host", default="0.0.0.0", help="Host to bind to (default: 0.0.0.0)")
    parser.add_argument("--port", default="8000", help="Port to bind to (default: 8000)")
    parser.add_argument("--no-reload", action="store_true", help="Disable auto-reload")
    args = parser.parse_args()
    
    # Get the directory where this script is located (template_service root)
    script_dir = os.path.dirname(os.path.abspath(__file__))
    
    # Change to the template_service directory to ensure consistent file paths
    os.chdir(script_dir)
    
    # Set environment variables if .env file exists
    env_file = os.path.join(script_dir, '.env')
    if os.path.exists(env_file):
        with open(env_file, 'r') as f:
            for line in f:
                line = line.strip()
                if line and not line.startswith('#') and '=' in line:
                    key, value = line.split('=', 1)
                    os.environ[key] = value
    
    # Build uvicorn command
    cmd = [
        sys.executable, "-m", "uvicorn", 
        "app.main:app", 
        "--host", args.host, 
        "--port", args.port
    ]
    
    if not args.no_reload:
        cmd.append("--reload")
    
    # Run the application using subprocess to avoid module path issues
    try:
        subprocess.run(cmd, cwd=script_dir)
    except KeyboardInterrupt:
        print("\nShutting down Template Service...")
    except FileNotFoundError:
        print("Error: uvicorn not installed. Please run 'pip install uvicorn'")
        sys.exit(1)
    except Exception as e:
        print(f"Error starting the application: {e}")
        sys.exit(1)

if __name__ == "__main__":
    main()
