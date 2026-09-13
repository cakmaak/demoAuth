package com.demoSec.demoauth.authorization.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(
        name = "permissions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_permission_tenant_resource_action",
                        columnNames = {
                                "tenant_id",
                                "resource_key",
                                "action_key"
                        }
                )
        }
)
public class Permission {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "resource_key", nullable = false, length = 150)
    private String resourceKey;

    @Column(name = "action_key", nullable = false, length = 100)
    private String actionKey;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "scope_required", nullable = false)
    private boolean scopeRequired;

    @Column(name = "is_system", nullable = false)
    private boolean system;

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}