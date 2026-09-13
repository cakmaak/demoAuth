package com.demoSec.demoauth.authorization.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(
        name = "role_inheritances",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_role_inheritance",
                        columnNames = {
                                "tenant_id",
                                "parent_role_id",
                                "child_role_id"
                        }
                )
        }
)
public class RoleInheritance {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "parent_role_id", nullable = false)
    private UUID parentRoleId;

    @Column(name = "child_role_id", nullable = false)
    private UUID childRoleId;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    public UUID getId() {
        return id;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public UUID getParentRoleId() {
        return parentRoleId;
    }

    public void setParentRoleId(UUID parentRoleId) {
        this.parentRoleId = parentRoleId;
    }

    public UUID getChildRoleId() {
        return childRoleId;
    }

    public void setChildRoleId(UUID childRoleId) {
        this.childRoleId = childRoleId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}