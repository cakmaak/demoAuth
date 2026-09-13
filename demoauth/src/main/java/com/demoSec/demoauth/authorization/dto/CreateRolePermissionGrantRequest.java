package com.demoSec.demoauth.authorization.dto;

import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

public class CreateRolePermissionGrantRequest {

    @NotNull
    private UUID roleId;

    @NotNull
    private UUID permissionId;

    @NotNull
    private UUID dataClassId;

    private UUID maskingPolicyId;

    @NotNull
    private String decision;

    @NotNull
    private Integer priority;

    private OffsetDateTime validFrom;

    private OffsetDateTime validTo;

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public UUID getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(UUID permissionId) {
        this.permissionId = permissionId;
    }

    public UUID getDataClassId() {
        return dataClassId;
    }

    public void setDataClassId(UUID dataClassId) {
        this.dataClassId = dataClassId;
    }

    public UUID getMaskingPolicyId() {
        return maskingPolicyId;
    }

    public void setMaskingPolicyId(UUID maskingPolicyId) {
        this.maskingPolicyId = maskingPolicyId;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
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