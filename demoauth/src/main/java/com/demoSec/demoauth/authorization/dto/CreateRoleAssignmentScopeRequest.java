package com.demoSec.demoauth.authorization.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

public class CreateRoleAssignmentScopeRequest {

    @NotNull
    private UUID roleAssignmentId;

    @NotBlank
    private String scopeType;

    private UUID organizationId;

    private UUID regionId;

    @NotBlank
    private String decision;

    private boolean includeDescendants = false;

    private OffsetDateTime validFrom;

    private OffsetDateTime validTo;

    public UUID getRoleAssignmentId() {
        return roleAssignmentId;
    }

    public void setRoleAssignmentId(UUID roleAssignmentId) {
        this.roleAssignmentId = roleAssignmentId;
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

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public boolean isIncludeDescendants() {
        return includeDescendants;
    }

    public void setIncludeDescendants(boolean includeDescendants) {
        this.includeDescendants = includeDescendants;
    }

    public OffsetDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(OffsetDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public OffsetDateTime getValidTo() {
        return validTo;
    }

    public void setValidTo(OffsetDateTime validTo) {
        this.validTo = validTo;
    }
}