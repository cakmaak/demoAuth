package com.demoSec.demoauth.authorization.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "principals",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_principal_identity",
                        columnNames = {
                                "tenant_id",
                                "identity_provider_id",
                                "principal_type",
                                "external_key"
                        }
                )
        }
)
public class Principal {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "identity_provider_id", nullable = false)
    private UUID identityProviderId;

    @Column(name = "party_id")
    private UUID partyId;

    @Column(name = "principal_type", nullable = false, length = 30)
    private String principalType;

    @Column(name = "external_key", nullable = false, length = 500)
    private String externalKey;

    @Column(name = "username_snapshot", length = 200)
    private String usernameSnapshot;

    @Column(name = "display_name_snapshot", length = 300)
    private String displayNameSnapshot;

    @Column(nullable = false, length = 30)
    private String status = "ACTIVE";

    @Column(name = "last_seen_at")
    private OffsetDateTime lastSeenAt;

    @Column(name = "synchronized_at")
    private OffsetDateTime synchronizedAt;

    public UUID getId() {
        return id;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public UUID getIdentityProviderId() {
        return identityProviderId;
    }

    public void setIdentityProviderId(UUID identityProviderId) {
        this.identityProviderId = identityProviderId;
    }

    public UUID getPartyId() {
        return partyId;
    }

    public void setPartyId(UUID partyId) {
        this.partyId = partyId;
    }

    public String getPrincipalType() {
        return principalType;
    }

    public void setPrincipalType(String principalType) {
        this.principalType = principalType;
    }

    public String getExternalKey() {
        return externalKey;
    }

    public void setExternalKey(String externalKey) {
        this.externalKey = externalKey;
    }

    public String getUsernameSnapshot() {
        return usernameSnapshot;
    }

    public void setUsernameSnapshot(String usernameSnapshot) {
        this.usernameSnapshot = usernameSnapshot;
    }

    public String getDisplayNameSnapshot() {
        return displayNameSnapshot;
    }

    public void setDisplayNameSnapshot(String displayNameSnapshot) {
        this.displayNameSnapshot = displayNameSnapshot;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OffsetDateTime getLastSeenAt() {
        return lastSeenAt;
    }

    public void setLastSeenAt(OffsetDateTime lastSeenAt) {
        this.lastSeenAt = lastSeenAt;
    }

    public OffsetDateTime getSynchronizedAt() {
        return synchronizedAt;
    }

    public void setSynchronizedAt(OffsetDateTime synchronizedAt) {
        this.synchronizedAt = synchronizedAt;
    }
}