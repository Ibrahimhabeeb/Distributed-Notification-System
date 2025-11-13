from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from sqlalchemy import text
import importlib
import time
from db.database import get_db
from core.config import settings

# Dynamically import redis to avoid static analyzer errors when the package is not installed
redis = None
try:
    redis = importlib.import_module("redis")
except Exception:
    redis = None
from core import schemas

router = APIRouter()

@router.get("/", response_model=schemas.StandardResponse[schemas.HealthData])
async def health_check(db: Session = Depends(get_db)):
    try:
        start_time = time.time()
        db_status = "connected"
        redis_status = "connected"
        
        try:
            # Use a SQL text object for compatibility with SQLAlchemy 2.x
            db.execute(text("SELECT 1"))
        except Exception:
            db_status = "disconnected"

        try:
            r = redis.from_url(settings.REDIS_URL)
            r.ping()
        except Exception:
            redis_status = "disconnected"

        uptime = time.time() - start_time
        
        status = "healthy" if db_status == "connected" and redis_status == "connected" else "unhealthy"

        data = schemas.HealthData(
            status=status,
            uptime=f"{uptime:.2f}s",
            db=db_status,
            redis=redis_status
        )

        return schemas.StandardResponse(
            success=True,
            data=data,
            message="Health check completed successfully"
        )
    except Exception as e:
        return schemas.StandardResponse(
            success=False,
            error=str(e),
            message="Health check failed"
        )
