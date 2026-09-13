package com.demoSec.demoauth.authorization.repository;

import com.demoSec.demoauth.authorization.entity.RoleAssignmentScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleAssignmentScopeRepository
        extends JpaRepository<RoleAssignmentScope, UUID> {

    List<RoleAssignmentScope> findByTenantIdAndRoleAssignmentId(
            UUID tenantId,
            UUID roleAssignmentId
    );

    List<RoleAssignmentScope> findByTenantIdAndRoleAssignmentIdAndDecision(
            UUID tenantId,
            UUID roleAssignmentId,
            String decision
    );
}