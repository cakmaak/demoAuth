package com.demoSec.demoauth.authorization.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "identity_providers",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_identity_provider_tenant_issuer",
                        columnNames = {"tenant_id", "issuer_uri"}
                )
        }
)
public class IdentityProvider {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "provider_type", nullable = false, length = 50)
    private String providerType;

    @Column(name = "issuer_uri", nullable = false, length = 500)
    private String issuerUri;

    @Column(name = "realm_name", length = 100)
    private String realmName;

    @Column(length = 200)
    private String audience;

    @Column(name = "claim_mapping", columnDefinition = "jsonb")
    private String claimMapping;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

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

    public String getProviderType() {
        return providerType;
    }

    public void setProviderType(String providerType) {
        this.providerType = providerType;
    }

    public String getIssuerUri() {
        return issuerUri;
    }

    public void setIssuerUri(String issuerUri) {
        this.issuerUri = issuerUri;
    }

    public String getRealmName() {
        return realmName;
    }

    public void setRealmName(String realmName) {
        this.realmName = realmName;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public String getClaimMapping() {
        return claimMapping;
    }

    public void setClaimMapping(String claimMapping) {
        this.claimMapping = claimMapping;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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