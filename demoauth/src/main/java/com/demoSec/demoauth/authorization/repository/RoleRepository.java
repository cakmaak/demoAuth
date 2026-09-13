package com.demoSec.demoauth.authorization.repository;

import com.demoSec.demoauth.authorization.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByTenantIdAndCode(UUID tenantId, String code);
    List<Role> findByTenantId(UUID tenantId);
}