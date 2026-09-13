package com.demoSec.demoauth.authorization.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.UUID;

public class CreateRoleAssignmentRequest {

    @NotNull
    private UUID principalId;

    @NotNull
    private UUID roleId;

    @NotNull
    @Size(max = 50)
    private String assignmentSource;

    @Size(max = 500)
    private String reason;

    private OffsetDateTime validFrom;

    private OffsetDateTime validTo;

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
}