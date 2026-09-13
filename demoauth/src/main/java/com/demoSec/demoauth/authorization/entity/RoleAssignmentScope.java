package com.demoSec.demoauth.authorization.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "role_assignment_scopes")
public class RoleAssignmentScope {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "role_assignment_id", nullable = false)
    private UUID roleAssignmentId;

    @Column(name = "scope_type", nullable = false, length = 30)
    private String scopeType;

    @Column(name = "organization_id")
    private UUID organizationId;

    @Column(name = "region_id")
    private UUID regionId;

    @Column(nullable = false, length = 20)
    private String decision;

    @Column(name = "include_descendants", nullable = false)
    private boolean includeDescendants = false;

    @Column(name = "valid_from")
    private OffsetDateTime validFrom;

    @Column(name = "valid_to")
    private OffsetDateTime validTo;

    public UUID getId() {
        return id;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

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