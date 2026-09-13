package com.demoSec.demoauth.authorization.dto;

public class EffectivePermissionResponse {

    private String resource;
    private String action;
    private boolean allowed;

    public EffectivePermissionResponse(
            String resource,
            String action,
            boolean allowed
    ) {
        this.resource = resource;
        this.action = action;
        this.allowed = allowed;
    }

    public String getResource() {
        return resource;
    }

    public String getAction() {
        return action;
    }

    public boolean isAllowed() {
        return allowed;
    }
}