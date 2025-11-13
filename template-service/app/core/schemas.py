from pydantic import BaseModel, Field
from typing import List, Dict, Any, Optional, Generic, TypeVar
from datetime import datetime

T = TypeVar('T')

# Standard Response Format
class PaginationMeta(BaseModel):
    total: int
    limit: int
    page: int
    total_pages: int
    has_next: bool
    has_previous: bool

class StandardResponse(BaseModel, Generic[T]):
    success: bool
    data: Optional[T] = None
    error: Optional[str] = None
    message: str
    meta: Optional[PaginationMeta] = None

# Base Schemas
class TemplateBase(BaseModel):
    template_key: str = Field(..., max_length=100)
    name: str = Field(..., max_length=150)
    description: Optional[str] = None
    category: str = Field(..., max_length=50)

class TemplateVersionBase(BaseModel):
    language: str = Field("en", max_length=5)
    subject: Optional[str] = Field(None, max_length=255)
    body: str
    variables: Optional[List[str]] = []

# Schemas for Creating
class TemplateCreate(TemplateBase):
    created_by: Optional[str] = None

class TemplateVersionCreate(TemplateVersionBase):
    template_id: str
    created_by: Optional[str] = None

# Schemas for Responses
class TemplateVersionResponse(TemplateVersionBase):
    id: str
    version_number: int
    created_at: datetime

    class Config:
        from_attributes = True

class TemplateResponse(TemplateBase):
    id: str
    created_at: datetime
    latest_version: Optional[int] = None
    versions: List[TemplateVersionResponse] = []

    class Config:
        from_attributes = True

class TemplateListItem(BaseModel):
    template_key: str
    category: str
    latest_version: int
    created_at: datetime

class LatestTemplateData(BaseModel):
    template_key: str
    language: str
    version_number: int
    subject: Optional[str]
    body: str
    variables: List[str]

class RenderData(BaseModel):
    subject: Optional[str]
    body: str

class HealthData(BaseModel):
    status: str
    uptime: str
    db: str
    redis: str

# Schema for Rendering
class RenderRequest(BaseModel):
    template_key: str
    language: str = "en"
    variables: Dict[str, Any]
