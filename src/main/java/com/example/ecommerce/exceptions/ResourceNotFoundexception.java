package com.example.ecommerce.exceptions;

public class ResourceNotFoundexception extends RuntimeException {
    String resourceName;
    String field;
    String fieldName;
    Long fieldId;

    public ResourceNotFoundexception() {
    }

    public ResourceNotFoundexception(String field, String resourceName, String fieldName) {
        super(String.format("%s not found with %s: %s",resourceName,field,fieldName));
        this.field = field;
        this.resourceName = resourceName;
        this.fieldName = fieldName;
    }

    public ResourceNotFoundexception(String resourceName, String field, Long fieldId) {
        super(String.format("%s not found with %s: %d",resourceName,field,fieldId));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldId = fieldId;
    }
}
