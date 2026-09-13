package com.demoSec.demoauth.authorization.repository;

import com.demoSec.demoauth.authorization.entity.RoleAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleAssignmentRepository
        extends JpaRepository<RoleAssignment, UUID> {

    List<RoleAssignment> findByTenantIdAndPrincipalIdAndRevokedAtIsNull(
            UUID tenantId,
            UUID principalId
    );

    List<RoleAssignment> findByTenantIdAndPrincipalIdAndRoleIdAndRevokedAtIsNull(
            UUID tenantId,
            UUID principalId,
            UUID roleId
    );
}