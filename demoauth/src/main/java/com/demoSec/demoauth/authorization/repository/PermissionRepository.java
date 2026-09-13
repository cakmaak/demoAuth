package com.demoSec.demoauth.authorization.repository;

import com.demoSec.demoauth.authorization.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, UUID> {

    Optional<Permission> findByTenantIdAndResourceKeyAndActionKey(
            UUID tenantId,
            String resourceKey,
            String actionKey
    );
    List<Permission> findByTenantId(UUID tenantId);
}