package com.demoSec.demoauth.authorization.repository;

import com.demoSec.demoauth.authorization.entity.RoleInheritance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleInheritanceRepository
        extends JpaRepository<RoleInheritance, UUID> {

    List<RoleInheritance> findByTenantIdAndParentRoleIdAndActiveTrue(
            UUID tenantId,
            UUID parentRoleId
    );

    List<RoleInheritance> findByTenantIdAndChildRoleIdAndActiveTrue(
            UUID tenantId,
            UUID childRoleId
    );
}