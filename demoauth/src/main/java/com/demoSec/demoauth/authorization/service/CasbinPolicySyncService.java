package com.demoSec.demoauth.authorization.service;

import com.demoSec.demoauth.authorization.entity.Permission;
import com.demoSec.demoauth.authorization.entity.Role;
import com.demoSec.demoauth.authorization.entity.RolePermissionGrant;
import com.demoSec.demoauth.authorization.repository.PermissionRepository;
import com.demoSec.demoauth.authorization.repository.RolePermissionGrantRepository;
import com.demoSec.demoauth.authorization.repository.RoleRepository;
import org.casbin.jcasbin.main.Enforcer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CasbinPolicySyncService {

    private final Enforcer enforcer;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionGrantRepository grantRepository;

    public CasbinPolicySyncService(
            Enforcer enforcer,
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            RolePermissionGrantRepository grantRepository
    ) {
        this.enforcer = enforcer;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.grantRepository = grantRepository;
    }

    @Transactional(readOnly = true)
    public void syncRole(
            UUID tenantId,
            UUID roleId
    ) {

        Role role = roleRepository
                .findById(roleId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Role bulunamadı"));

        if (!role.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException(
                    "Role bu tenant'a ait değil"
            );
        }

        List<RolePermissionGrant> grants =
                grantRepository.findByTenantIdAndRoleId(
                        tenantId,
                        roleId
                );

        for (RolePermissionGrant grant : grants) {

            if (!grant.getDecision().equals("ALLOW")) {
                continue;
            }

            Permission permission =
                    permissionRepository
                            .findById(grant.getPermissionId())
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "Permission bulunamadı"
                                    ));

            enforcer.addPolicy(
                    role.getCode(),
                    permission.getResourceKey(),
                    permission.getActionKey()
            );
        }
    }
}