package com.demoSec.demoauth.authorization.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class CreateAuthorizationConstraintRequest {

    @NotNull
    private UUID rolePermissionGrantId;

    @NotBlank
    @Size(max = 50)
    private String constraintKey;

    private String parameters;

    private boolean required = false;

    public UUID getRolePermissionGrantId() {
        return rolePermissionGrantId;
    }

    public void setRolePermissionGrantId(UUID rolePermissionGrantId) {
        this.rolePermissionGrantId = rolePermissionGrantId;
    }

    public String getConstraintKey() {
        return constraintKey;
    }

    public void setConstraintKey(String constraintKey) {
        this.constraintKey = constraintKey;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}