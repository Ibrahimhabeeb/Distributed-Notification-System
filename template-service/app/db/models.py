import uuid
from sqlalchemy import Column, String, Text, DateTime, func, ForeignKey, Integer, Boolean, JSON
from sqlalchemy.dialects.postgresql import UUID
from sqlalchemy.orm import relationship
from db.database import Base

class Template(Base):
    __tablename__ = "templates"

    id = Column(String, primary_key=True, default=lambda: str(uuid.uuid4()))
    template_key = Column(String(100), unique=True, index=True, nullable=False)
    name = Column(String(150), nullable=False)
    description = Column(Text)
    category = Column(String(50), nullable=False)
    created_at = Column(DateTime(timezone=True), server_default=func.now())
    updated_at = Column(DateTime(timezone=True), onupdate=func.now())
    is_deleted = Column(Boolean, default=False)

    versions = relationship("TemplateVersion", back_populates="template", cascade="all, delete-orphan")

class TemplateVersion(Base):
    __tablename__ = "template_versions"

    id = Column(String, primary_key=True, default=lambda: str(uuid.uuid4()))
    template_id = Column(String, ForeignKey("templates.id"), nullable=False)
    version_number = Column(Integer, nullable=False)
    language = Column(String(5), default="en")
    subject = Column(String(255))
    body = Column(Text, nullable=False)
    variables = Column(JSON)
    created_at = Column(DateTime(timezone=True), server_default=func.now())
    updated_at = Column(DateTime(timezone=True), onupdate=func.now())

    template = relationship("Template", back_populates="versions")
