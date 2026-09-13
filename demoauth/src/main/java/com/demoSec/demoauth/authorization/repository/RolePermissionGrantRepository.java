package com.demoSec.demoauth.authorization.repository;

import com.demoSec.demoauth.authorization.entity.RolePermissionGrant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RolePermissionGrantRepository
        extends JpaRepository<RolePermissionGrant, UUID> {

    List<RolePermissionGrant> findByTenantId(
            UUID tenantId
    );

    List<RolePermissionGrant> findByTenantIdAndRoleId(
            UUID tenantId,
            UUID roleId
    );

    List<RolePermissionGrant> findByTenantIdAndRoleIdAndPermissionId(
            UUID tenantId,
            UUID roleId,
            UUID permissionId
    );
}