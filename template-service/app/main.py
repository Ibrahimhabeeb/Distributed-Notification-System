import sys
import os

# Get the directory where this file is located (app directory)
app_dir = os.path.dirname(os.path.abspath(__file__))
# Get the parent directory (template_service root)
template_service_dir = os.path.dirname(app_dir)

# Change working directory to template_service root for consistent file paths
os.chdir(template_service_dir)

# Add app directory to Python path for imports
sys.path.insert(0, app_dir)

from fastapi import FastAPI
from api.routes import templates, versions, health
from db.database import engine, Base
from core import schemas

Base.metadata.create_all(bind=engine)

app = FastAPI(
    title="Template Service",
    description="A microservice for managing notification templates.",
    version="0.1.0"
)

app.include_router(health.router, prefix="/health", tags=["Health"])
app.include_router(templates.router, prefix="/templates", tags=["Templates"])
app.include_router(versions.router, prefix="/templates", tags=["Template Versions"])

@app.get("/", response_model=schemas.StandardResponse[None])
async def root():
    return schemas.StandardResponse(
        success=True,
        message="Welcome to the Template Service!"
    )

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
