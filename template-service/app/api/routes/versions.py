from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List
from db.database import get_db
from db import models
from core import schemas

router = APIRouter()

@router.post("/add_version", response_model=schemas.StandardResponse[schemas.TemplateVersionResponse])
def add_template_version(version: schemas.TemplateVersionCreate, db: Session = Depends(get_db)):
    try:
        template = db.query(models.Template).filter(
            models.Template.id == version.template_id, 
            models.Template.is_deleted == False
        ).first()
        
        if not template:
            return schemas.StandardResponse(
                success=False,
                error="Template not found",
                message="Template not found"
            )

        latest_version = db.query(models.TemplateVersion).filter(
            models.TemplateVersion.template_id == version.template_id
        ).order_by(models.TemplateVersion.version_number.desc()).first()
        
        new_version_number = (latest_version.version_number + 1) if latest_version else 1

        version_data = version.dict()
        # Remove created_by if it exists since it's not in the model
        version_data.pop('created_by', None)
        
        db_version = models.TemplateVersion(
            **version_data,
            version_number=new_version_number
        )
        db.add(db_version)
        db.commit()
        db.refresh(db_version)
        
        return schemas.StandardResponse(
            success=True,
            data=db_version,
            message="Template version added successfully"
        )
    except Exception as e:
        return schemas.StandardResponse(
            success=False,
            error=str(e),
            message="Failed to add template version"
        )

@router.get("/{template_id}/versions", response_model=schemas.StandardResponse[List[schemas.TemplateVersionResponse]])
def get_template_versions(template_id: str, db: Session = Depends(get_db)):
    try:
        versions = db.query(models.TemplateVersion).filter(
            models.TemplateVersion.template_id == template_id
        ).all()
        
        return schemas.StandardResponse(
            success=True,
            data=versions,
            message="Template versions retrieved successfully"
        )
    except Exception as e:
        return schemas.StandardResponse(
            success=False,
            error=str(e),
            message="Failed to retrieve template versions"
        )

@router.put("/version/{version_id}", response_model=schemas.StandardResponse[schemas.TemplateVersionResponse])
def update_template_version(version_id: str, version: schemas.TemplateVersionBase, db: Session = Depends(get_db)):
    try:
        db_version = db.query(models.TemplateVersion).filter(
            models.TemplateVersion.id == version_id
        ).first()
        
        if not db_version:
            return schemas.StandardResponse(
                success=False,
                error="Version not found",
                message="Version not found"
            )

        for key, value in version.dict().items():
            setattr(db_version, key, value)
        
        db.commit()
        db.refresh(db_version)
        
        return schemas.StandardResponse(
            success=True,
            data=db_version,
            message="Template version updated successfully"
        )
    except Exception as e:
        return schemas.StandardResponse(
            success=False,
            error=str(e),
            message="Failed to update template version"
        )

@router.delete("/version/{version_id}", response_model=schemas.StandardResponse[None])
def delete_template_version(version_id: str, db: Session = Depends(get_db)):
    try:
        db_version = db.query(models.TemplateVersion).filter(
            models.TemplateVersion.id == version_id
        ).first()
        
        if not db_version:
            return schemas.StandardResponse(
                success=False,
                error="Version not found",
                message="Version not found"
            )
        
        db.delete(db_version)
        db.commit()
        
        return schemas.StandardResponse(
            success=True,
            message="Template version deleted successfully"
        )
    except Exception as e:
        return schemas.StandardResponse(
            success=False,
            error=str(e),
            message="Failed to delete template version"
        )
