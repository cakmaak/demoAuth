package com.demoSec.demoauth.authorization.service;

import com.demoSec.demoauth.authorization.dto.PermissionMatrixResponse;
import com.demoSec.demoauth.authorization.entity.Permission;
import com.demoSec.demoauth.authorization.entity.Role;
import com.demoSec.demoauth.authorization.entity.RoleInheritance;
import com.demoSec.demoauth.authorization.entity.RolePermissionGrant;
import com.demoSec.demoauth.authorization.repository.PermissionRepository;
import com.demoSec.demoauth.authorization.repository.RoleInheritanceRepository;
import com.demoSec.demoauth.authorization.repository.RolePermissionGrantRepository;
import com.demoSec.demoauth.authorization.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PermissionMatrixService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionGrantRepository grantRepository;
    private final RoleInheritanceRepository roleInheritanceRepository;

    public PermissionMatrixService(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            RolePermissionGrantRepository grantRepository,
            RoleInheritanceRepository roleInheritanceRepository
    ) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.grantRepository = grantRepository;
        this.roleInheritanceRepository = roleInheritanceRepository;
    }

    public List<PermissionMatrixResponse> getMatrix(UUID tenantId) {

        List<Role> roles =
                roleRepository.findByTenantId(tenantId);

        List<Permission> permissions =
                permissionRepository.findByTenantId(tenantId);

        List<RolePermissionGrant> grants =
                grantRepository.findByTenantId(tenantId);

        List<PermissionMatrixResponse> result =
                new ArrayList<>();

        for (Role role : roles) {

            Set<UUID> effectiveRoleIds =
                    new HashSet<>();

            effectiveRoleIds.add(role.getId());

            addInheritedRoles(
                    tenantId,
                    role.getId(),
                    effectiveRoleIds
            );

            for (Permission permission : permissions) {

                boolean allowed = false;
                boolean inherited = false;
                UUID grantId = null;

                for (UUID effectiveRoleId : effectiveRoleIds) {

                    Optional<RolePermissionGrant> matchingGrant =
                            grants.stream()
                                    .filter(grant ->
                                            grant.getRoleId().equals(effectiveRoleId)
                                                    && grant.getPermissionId().equals(permission.getId())
                                                    && "ALLOW".equals(grant.getDecision())
                                    )
                                    .findFirst();

                    if (matchingGrant.isPresent()) {

                        allowed = true;
                        grantId = matchingGrant.get().getId();

                        if (!effectiveRoleId.equals(role.getId())) {
                            inherited = true;
                        }

                        break;
                    }
                }

                result.add(
                        new PermissionMatrixResponse(
                                role.getId(),
                                permission.getId(),
                                grantId,
                                role.getCode(),
                                role.getName(),
                                permission.getResourceKey(),
                                permission.getActionKey(),
                                allowed,
                                inherited,
                                permission.getName()
                        )
                );
            }
        }

        return result;
    }

    private void addInheritedRoles(
            UUID tenantId,
            UUID roleId,
            Set<UUID> roleIds
    ) {

        List<RoleInheritance> inheritances =
                roleInheritanceRepository
                        .findByTenantIdAndParentRoleIdAndActiveTrue(
                                tenantId,
                                roleId
                        );

        for (RoleInheritance inheritance : inheritances) {

            UUID inheritedRoleId =
                    inheritance.getChildRoleId();

            if (roleIds.add(inheritedRoleId)) {

                addInheritedRoles(
                        tenantId,
                        inheritedRoleId,
                        roleIds
                );
            }
        }
    }
}