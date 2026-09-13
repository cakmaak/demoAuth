package com.demoSec.demoauth.authorization.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(
        name = "masking_policies",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_masking_policy_tenant_code",
                        columnNames = {"tenant_id", "code"}
                )
        }
)
public class MaskingPolicy {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false, length = 100)
    private String code;

    @Column(name = "strategy_key", nullable = false, length = 50)
    private String strategyKey;

    @Column(columnDefinition = "jsonb")
    private String parameters;

    @Column(name = "policy_reference", length = 300)
    private String policyReference;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStrategyKey() {
        return strategyKey;
    }

    public void setStrategyKey(String strategyKey) {
        this.strategyKey = strategyKey;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getPolicyReference() {
        return policyReference;
    }

    public void setPolicyReference(String policyReference) {
        this.policyReference = policyReference;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}