package com.demoSec.demoauth.authorization.service;

import com.demoSec.demoauth.authorization.dto.CreatePermissionRequest;
import com.demoSec.demoauth.authorization.entity.Permission;
import com.demoSec.demoauth.authorization.repository.PermissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(
            PermissionRepository permissionRepository
    ) {
        this.permissionRepository = permissionRepository;
    }

    @Transactional
    public Permission createPermission(
            UUID tenantId,
            CreatePermissionRequest request
    ) {

        if (permissionRepository
                .findByTenantIdAndResourceKeyAndActionKey(
                        tenantId,
                        request.getResourceKey(),
                        request.getActionKey()
                )
                .isPresent()) {

            throw new IllegalStateException(
                    "Bu permission zaten mevcut: "
                            + request.getResourceKey()
                            + " / "
                            + request.getActionKey()
            );
        }

        Permission permission = new Permission();

        permission.setTenantId(tenantId);
        permission.setResourceKey(request.getResourceKey());
        permission.setActionKey(request.getActionKey());
        permission.setName(request.getName());
        permission.setScopeRequired(request.isScopeRequired());
        permission.setSystem(request.isSystem());
        permission.setActive(true);

        return permissionRepository.save(permission);
    }
    public List<Permission> getPermissions(UUID tenantId) {

        return permissionRepository.findByTenantId(tenantId);
    }
}