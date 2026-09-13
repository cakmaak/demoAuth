package com.demoSec.demoauth.authorization.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(
        name = "data_classes",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_data_class_tenant_code",
                        columnNames = {"tenant_id", "code"}
                )
        }
)
public class DataClass {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "parent_data_class_id")
    private UUID parentDataClassId;

    @Column(nullable = false, length = 100)
    private String code;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "sensitivity_rank", nullable = false)
    private Integer sensitivityRank;

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

    public UUID getParentDataClassId() {
        return parentDataClassId;
    }

    public void setParentDataClassId(UUID parentDataClassId) {
        this.parentDataClassId = parentDataClassId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSensitivityRank() {
        return sensitivityRank;
    }

    public void setSensitivityRank(Integer sensitivityRank) {
        this.sensitivityRank = sensitivityRank;
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