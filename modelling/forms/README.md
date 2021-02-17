### Further examples of Camunda controls:
[https://docs.camunda.org/manual/latest/reference/embedded-forms/controls/](https://docs.camunda.org/manual/latest/reference/embedded-forms/controls/)

### Deploy resources/files:
```bash
curl --location --request POST 'https://{{camunda-instance}}/engine-rest/deployment/create' \
--header 'Accept: application/json' \
--form 'deployment-name=Example' \
--form 'enable-duplicate-filtering=false' \
--form 'deploy-changed-only=false' \
--form 'deployment-source=REST' \
--form 'embedded-forms.bpmn=@/path/to/file' \
--form 'InputForm.html=@/path/to/file' \
--form 'ShowForm.html=@/path/to/file'
```

Example with tenant id:
```bash
curl --location --request POST 'https://{{camunda-instance}}/engine-rest/deployment/create' \
--header 'Accept: application/json' \
--form 'deployment-name=Example' \
--form 'enable-duplicate-filtering=false' \
--form 'deploy-changed-only=false' \
--form 'deployment-source=REST' \
--form 'tenant-id=tenantId' \
--form 'embedded-forms.bpmn=@/path/to/file' \
--form 'InputForm.html=@/path/to/file' \
--form 'ShowForm.html=@/path/to/file'
```