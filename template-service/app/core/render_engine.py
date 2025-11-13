from jinja2 import Environment, FileSystemLoader
from typing import Dict, Any
from db.models import TemplateVersion

# In a real application, you might want to configure the loader differently
# For example, loading templates from a database or a distributed cache.
# For simplicity, we'll assume templates are not loaded from files here,
# but we'll use Jinja2's string loading capabilities.
env = Environment()

def render(template_version: TemplateVersion, variables: Dict[str, Any]):
    """
    Renders a template with the given variables.
    """
    # Render subject
    subject_template = env.from_string(template_version.subject or "")
    rendered_subject = subject_template.render(variables)

    # Render body
    body_template = env.from_string(template_version.body)
    rendered_body = body_template.render(variables)

    return rendered_subject, rendered_body
