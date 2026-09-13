package com.demoSec.demoauth.authorization.repository;

import com.demoSec.demoauth.authorization.entity.MaskingPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MaskingPolicyRepository
        extends JpaRepository<MaskingPolicy, UUID> {

    Optional<MaskingPolicy> findByTenantIdAndCode(
            UUID tenantId,
            String code
    );
}