import os
from pydantic_settings import BaseSettings

class Settings(BaseSettings):
    DATABASE_URL: str
    REDIS_URL: str

    class Config:
        # The working directory is set to template_service root in main.py
        # so .env will be found there
        env_file = ".env"

settings = Settings()
