package com.demoSec.demoauth.authorization.service;

import com.demoSec.demoauth.authorization.entity.Role;
import com.demoSec.demoauth.authorization.entity.RoleAssignment;
import com.demoSec.demoauth.authorization.entity.RoleInheritance;
import com.demoSec.demoauth.authorization.repository.RoleAssignmentRepository;
import com.demoSec.demoauth.authorization.repository.RoleInheritanceRepository;
import com.demoSec.demoauth.authorization.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PrincipalRoleService {

    private final RoleAssignmentRepository roleAssignmentRepository;
    private final RoleRepository roleRepository;
    private final RoleInheritanceRepository roleInheritanceRepository;

    public PrincipalRoleService(
            RoleAssignmentRepository roleAssignmentRepository,
            RoleRepository roleRepository,
            RoleInheritanceRepository roleInheritanceRepository
    ) {
        this.roleAssignmentRepository = roleAssignmentRepository;
        this.roleRepository = roleRepository;
        this.roleInheritanceRepository = roleInheritanceRepository;
    }

    public List<Role> getRoles(
            UUID tenantId,
            UUID principalId
    ) {

        List<RoleAssignment> assignments =
                roleAssignmentRepository
                        .findByTenantIdAndPrincipalIdAndRevokedAtIsNull(
                                tenantId,
                                principalId
                        );

        Set<UUID> roleIds = new HashSet<>();

        for (RoleAssignment assignment : assignments) {

            roleIds.add(assignment.getRoleId());

            addInheritedRoles(
                    tenantId,
                    assignment.getRoleId(),
                    roleIds
            );
        }

        return roleIds.stream()
                .map(roleId ->
                        roleRepository.findById(roleId)
                                .orElseThrow(() ->
                                        new IllegalArgumentException(
                                                "Role bulunamadı"
                                        )
                                )
                )
                .toList();
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