package com.demoSec.demoauth.authorization.repository;

import com.demoSec.demoauth.authorization.entity.AuthorizationConstraint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuthorizationConstraintRepository
        extends JpaRepository<AuthorizationConstraint, UUID> {

    List<AuthorizationConstraint> findByTenantIdAndRolePermissionGrantId(
            UUID tenantId,
            UUID rolePermissionGrantId
    );

}