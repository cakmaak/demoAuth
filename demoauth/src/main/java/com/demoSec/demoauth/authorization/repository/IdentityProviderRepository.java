package com.demoSec.demoauth.authorization.repository;

import com.demoSec.demoauth.authorization.entity.IdentityProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IdentityProviderRepository
        extends JpaRepository<IdentityProvider, UUID> {

    Optional<IdentityProvider> findByTenantIdAndIssuerUri(
            UUID tenantId,
            String issuerUri
    );

    Optional<IdentityProvider> findByIssuerUri(String issuerUri);
}