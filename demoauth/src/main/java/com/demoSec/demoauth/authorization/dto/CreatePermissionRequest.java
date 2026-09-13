package com.demoSec.demoauth.authorization.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreatePermissionRequest {

    @NotBlank
    @Size(max = 150)
    private String resourceKey;

    @NotBlank
    @Size(max = 100)
    private String actionKey;

    @NotBlank
    @Size(max = 200)
    private String name;

    private boolean scopeRequired = false;

    private boolean system = false;

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public String getActionKey() {
        return actionKey;
    }

    public void setActionKey(String actionKey) {
        this.actionKey = actionKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isScopeRequired() {
        return scopeRequired;
    }

    public void setScopeRequired(boolean scopeRequired) {
        this.scopeRequired = scopeRequired;
    }

    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }
}