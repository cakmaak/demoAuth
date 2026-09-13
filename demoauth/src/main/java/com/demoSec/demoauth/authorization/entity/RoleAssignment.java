package com.demoSec.demoauth.authorization.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "role_assignments")
public class RoleAssignment {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "principal_id", nullable = false)
    private UUID principalId;

    @Column(name = "role_id", nullable = false)
    private UUID roleId;

    @Column(name = "assigned_by_principal_id")
    private UUID assignedByPrincipalId;

    @Column(name = "assignment_source", nullable = false, length = 50)
    private String assignmentSource;

    @Column(length = 500)
    private String reason;

    @Column(name = "valid_from")
    private OffsetDateTime validFrom;

    @Column(name = "valid_to")
    private OffsetDateTime validTo;

    @Column(name = "revoked_at")
    private OffsetDateTime revokedAt;

    public UUID getId() {
        return id;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public UUID getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(UUID principalId) {
        this.principalId = principalId;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public UUID getAssignedByPrincipalId() {
        return assignedByPrincipalId;
    }

    public void setAssignedByPrincipalId(UUID assignedByPrincipalId) {
        this.assignedByPrincipalId = assignedByPrincipalId;
    }

    public String getAssignmentSource() {
        return assignmentSource;
    }

    public void setAssignmentSource(String assignmentSource) {
        this.assignmentSource = assignmentSource;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public OffsetDateTime getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(OffsetDateTime revokedAt) {
        this.revokedAt = revokedAt;
    }
}