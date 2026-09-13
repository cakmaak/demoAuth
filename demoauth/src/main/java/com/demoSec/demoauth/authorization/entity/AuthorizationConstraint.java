package com.demoSec.demoauth.authorization.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "authorization_constraints")
public class AuthorizationConstraint {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "role_permission_grant_id", nullable = false)
    private UUID rolePermissionGrantId;

    @Column(name = "constraint_key", nullable = false, length = 50)
    private String constraintKey;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String parameters;

    @Column(name = "is_required", nullable = false)
    private boolean required = false;

    public UUID getId() {
        return id;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

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