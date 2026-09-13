
package com.demoSec.demoauth.authorization.dto;

import java.util.UUID;

public class AuthorizationContext {

    private UUID targetPrincipalId;
    private String scopeType;
    private UUID organizationId;
    private UUID regionId;

    public UUID getTargetPrincipalId() {
        return targetPrincipalId;
    }

    public void setTargetPrincipalId(UUID targetPrincipalId) {
        this.targetPrincipalId = targetPrincipalId;
    }

    public String getScopeType() {
        return scopeType;
    }

    public void setScopeType(String scopeType) {
        this.scopeType = scopeType;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(UUID organizationId) {
        this.organizationId = organizationId;
    }

    public UUID getRegionId() {
        return regionId;
    }

    public void setRegionId(UUID regionId) {
        this.regionId = regionId;
    }
}

