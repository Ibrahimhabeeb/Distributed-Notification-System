from fastapi import APIRouter, Depends, HTTPException, Query
from sqlalchemy.orm import Session
from typing import List, Optional
from db.database import get_db
from db import models
from core import schemas
import math

router = APIRouter()

@router.post("/", response_model=schemas.StandardResponse[schemas.TemplateResponse])
def create_template(template: schemas.TemplateCreate, db: Session = Depends(get_db)):
    try:
        # Remove created_by if it exists since it's not in the model
        template_data = template.dict()
        template_data.pop('created_by', None)
        
        db_template = models.Template(**template_data)
        db.add(db_template)
        db.commit()
        db.refresh(db_template)
        return schemas.StandardResponse(
            success=True,
            data=db_template,
            message="Template created successfully"
        )
    except Exception as e:
        return schemas.StandardResponse(
            success=False,
            error=str(e),
            message="Failed to create template"
        )

@router.get("/", response_model=schemas.StandardResponse[List[schemas.TemplateListItem]])
def get_templates(
    category: Optional[str] = None,
    page: int = Query(1, ge=1),
    limit: int = Query(20, ge=1, le=100),
    db: Session = Depends(get_db)
):
    try:
        query = db.query(models.Template).filter(models.Template.is_deleted == False)
        if category:
            query = query.filter(models.Template.category == category)

        total = query.count()
        offset = (page - 1) * limit
        templates = query.offset(offset).limit(limit).all()

        template_list = []
        for t in templates:
            latest_version = db.query(models.TemplateVersion).filter(
                models.TemplateVersion.template_id == t.id
            ).order_by(models.TemplateVersion.version_number.desc()).first()
            
            template_list.append(schemas.TemplateListItem(
                template_key=t.template_key,
                category=t.category,
                latest_version=latest_version.version_number if latest_version else 0,
                created_at=t.created_at
            ))

        meta = schemas.PaginationMeta(
            total=total,
            limit=limit,
            page=page,
            total_pages=math.ceil(total / limit),
            has_next=(page * limit) < total,
            has_previous=page > 1
        )

        return schemas.StandardResponse(
            success=True,
            data=template_list,
            message="Templates retrieved successfully",
            meta=meta
        )
    except Exception as e:
        return schemas.StandardResponse(
            success=False,
            error=str(e),
            message="Failed to retrieve templates"
        )

@router.get("/get_latest", response_model=schemas.StandardResponse[schemas.LatestTemplateData])
def get_latest_template_version(
    template_key: str,
    language: str = "en",
    db: Session = Depends(get_db)
):
    try:
        template = db.query(models.Template).filter(
            models.Template.template_key == template_key, 
            models.Template.is_deleted == False
        ).first()
        
        if not template:
            return schemas.StandardResponse(
                success=False,
                error="Template not found",
                message="Template not found"
            )

        latest_version = db.query(models.TemplateVersion).filter(
            models.TemplateVersion.template_id == template.id,
            models.TemplateVersion.language == language
        ).order_by(models.TemplateVersion.version_number.desc()).first()

        if not latest_version:
            return schemas.StandardResponse(
                success=False,
                error="No version found for the specified language",
                message="No version found for the specified language"
            )

        data = schemas.LatestTemplateData(
            template_key=template_key,
            language=language,
            version_number=latest_version.version_number,
            subject=latest_version.subject,
            body=latest_version.body,
            variables=latest_version.variables or []
        )

        return schemas.StandardResponse(
            success=True,
            data=data,
            message="Fetched latest template successfully"
        )
    except Exception as e:
        return schemas.StandardResponse(
            success=False,
            error=str(e),
            message="Failed to fetch latest template"
        )

@router.post("/render", response_model=schemas.StandardResponse[schemas.RenderData])
def render_template(
    request: schemas.RenderRequest,
    db: Session = Depends(get_db)
):
    try:
        from core.render_engine import render
        
        template = db.query(models.Template).filter(
            models.Template.template_key == request.template_key, 
            models.Template.is_deleted == False
        ).first()
        
        if not template:
            return schemas.StandardResponse(
                success=False,
                error="Template not found",
                message="Template not found"
            )

        latest_version = db.query(models.TemplateVersion).filter(
            models.TemplateVersion.template_id == template.id,
            models.TemplateVersion.language == request.language
        ).order_by(models.TemplateVersion.version_number.desc()).first()

        if not latest_version:
            return schemas.StandardResponse(
                success=False,
                error="No version found for the specified language",
                message="No version found for the specified language"
            )

        rendered_subject, rendered_body = render(latest_version, request.variables)
        
        data = schemas.RenderData(
            subject=rendered_subject,
            body=rendered_body
        )

        return schemas.StandardResponse(
            success=True,
            data=data,
            message="Render successful"
        )
    except Exception as e:
        return schemas.StandardResponse(
            success=False,
            error=str(e),
            message="Failed to render template"
        )

@router.get("/{template_id}", response_model=schemas.StandardResponse[schemas.TemplateResponse])
def get_template(template_id: str, db: Session = Depends(get_db)):
    try:
        template = db.query(models.Template).filter(
            models.Template.id == template_id, 
            models.Template.is_deleted == False
        ).first()
        
        if not template:
            return schemas.StandardResponse(
                success=False,
                error="Template not found",
                message="Template not found"
            )
        
        latest_version = db.query(models.TemplateVersion).filter(
            models.TemplateVersion.template_id == template.id
        ).order_by(models.TemplateVersion.version_number.desc()).first()
        
        template.latest_version = latest_version.version_number if latest_version else 0
        
        return schemas.StandardResponse(
            success=True,
            data=template,
            message="Template retrieved successfully"
        )
    except Exception as e:
        return schemas.StandardResponse(
            success=False,
            error=str(e),
            message="Failed to retrieve template"
        )

@router.delete("/{template_id}", response_model=schemas.StandardResponse[None])
def delete_template(template_id: str, db: Session = Depends(get_db)):
    try:
        template = db.query(models.Template).filter(models.Template.id == template_id).first()
        
        if not template:
            return schemas.StandardResponse(
                success=False,
                error="Template not found",
                message="Template not found"
            )
        
        template.is_deleted = True
        db.commit()
        
        return schemas.StandardResponse(
            success=True,
            message="Template deleted successfully"
        )
    except Exception as e:
        return schemas.StandardResponse(
            success=False,
            error=str(e),
            message="Failed to delete template"
        )
