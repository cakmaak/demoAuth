package com.demoSec.demoauth.authorization.repository;

import com.demoSec.demoauth.authorization.entity.Principal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PrincipalRepository extends JpaRepository<Principal, UUID> {

    Optional<Principal> findByTenantIdAndIdentityProviderIdAndPrincipalTypeAndExternalKey(
            UUID tenantId,
            UUID identityProviderId,
            String principalType,
            String externalKey
    );
}