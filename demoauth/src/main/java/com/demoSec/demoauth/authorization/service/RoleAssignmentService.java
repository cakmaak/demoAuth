package com.demoSec.demoauth.authorization.service;

import com.demoSec.demoauth.authorization.dto.CreateRoleAssignmentRequest;
import com.demoSec.demoauth.authorization.entity.Principal;
import com.demoSec.demoauth.authorization.entity.Role;
import com.demoSec.demoauth.authorization.entity.RoleAssignment;
import com.demoSec.demoauth.authorization.repository.PrincipalRepository;
import com.demoSec.demoauth.authorization.repository.RoleAssignmentRepository;
import com.demoSec.demoauth.authorization.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class RoleAssignmentService {

    private final RoleAssignmentRepository roleAssignmentRepository;
    private final PrincipalRepository principalRepository;
    private final RoleRepository roleRepository;

    public RoleAssignmentService(
            RoleAssignmentRepository roleAssignmentRepository,
            PrincipalRepository principalRepository,
            RoleRepository roleRepository
    ) {
        this.roleAssignmentRepository = roleAssignmentRepository;
        this.principalRepository = principalRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public RoleAssignment assignRole(
            UUID tenantId,
            CreateRoleAssignmentRequest request
    ) {

        // Principal gerçekten bu tenant'a ait mi?
        Principal principal = principalRepository
                .findById(request.getPrincipalId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Principal bulunamadı"
                        )
                );

        if (!principal.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException(
                    "Principal bu tenant'a ait değil"
            );
        }

        // Role gerçekten bu tenant'a ait mi?
        Role role = roleRepository
                .findById(request.getRoleId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Role bulunamadı"
                        )
                );

        if (!role.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException(
                    "Role bu tenant'a ait değil"
            );
        }

        // Aynı aktif atama zaten var mı?
        if (roleAssignmentRepository
                .findByTenantIdAndPrincipalIdAndRoleIdAndRevokedAtIsNull(
                        tenantId,
                        request.getPrincipalId(),
                        request.getRoleId()
                )
                .stream()
                .anyMatch(existing ->
                        existing.getRevokedAt() == null
                )) {

            throw new IllegalStateException(
                    "Bu Principal'a bu rol zaten atanmış"
            );
        }

        RoleAssignment assignment = new RoleAssignment();

        assignment.setTenantId(tenantId);
        assignment.setPrincipalId(request.getPrincipalId());
        assignment.setRoleId(request.getRoleId());
        assignment.setAssignmentSource(
                request.getAssignmentSource()
        );
        assignment.setReason(request.getReason());

        assignment.setValidFrom(
                request.getValidFrom() != null
                        ? request.getValidFrom()
                        : OffsetDateTime.now()
        );

        assignment.setValidTo(request.getValidTo());

        assignment.setRevokedAt(null);

        return roleAssignmentRepository.save(assignment);
    }
}