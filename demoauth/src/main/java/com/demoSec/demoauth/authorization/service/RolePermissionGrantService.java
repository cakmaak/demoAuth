package com.demoSec.demoauth.authorization.service;

import com.demoSec.demoauth.authorization.dto.CreateRolePermissionGrantRequest;
import com.demoSec.demoauth.authorization.dto.RolePermissionGrantResponse;
import com.demoSec.demoauth.authorization.entity.Permission;
import com.demoSec.demoauth.authorization.entity.Role;
import com.demoSec.demoauth.authorization.entity.RolePermissionGrant;
import com.demoSec.demoauth.authorization.entity.DataClass;
import com.demoSec.demoauth.authorization.repository.PermissionRepository;
import com.demoSec.demoauth.authorization.repository.RolePermissionGrantRepository;
import com.demoSec.demoauth.authorization.repository.RoleRepository;
import com.demoSec.demoauth.authorization.repository.DataClassRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.benmanes.caffeine.cache.Cache;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class RolePermissionGrantService {

    private final RolePermissionGrantRepository grantRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final DataClassRepository dataClassRepository;
    private final CasbinPolicySyncService casbinPolicySyncService;
    private final Cache<String, Boolean> authorizationCache;

    public RolePermissionGrantService(
            RolePermissionGrantRepository grantRepository,
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            DataClassRepository dataClassRepository,
            CasbinPolicySyncService casbinPolicySyncService,
            Cache<String, Boolean> authorizationCache
    ) {
        this.grantRepository = grantRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.dataClassRepository = dataClassRepository;
        this.casbinPolicySyncService = casbinPolicySyncService;
        this.authorizationCache = authorizationCache;
    }

    @Transactional
    public RolePermissionGrant createGrant(
            UUID tenantId,
            CreateRolePermissionGrantRequest request
    ) {

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

        Permission permission = permissionRepository
                .findById(request.getPermissionId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Permission bulunamadı"
                        )
                );

        if (!permission.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException(
                    "Permission bu tenant'a ait değil"
            );
        }

        DataClass dataClass = dataClassRepository
                .findById(request.getDataClassId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "DataClass bulunamadı"
                        )
                );

        if (!dataClass.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException(
                    "DataClass bu tenant'a ait değil"
            );
        }

        if (!request.getDecision().equals("ALLOW")
                && !request.getDecision().equals("DENY")) {

            throw new IllegalArgumentException(
                    "Decision sadece ALLOW veya DENY olabilir"
            );
        }

        RolePermissionGrant grant =
                new RolePermissionGrant();

        grant.setTenantId(tenantId);
        grant.setRoleId(request.getRoleId());
        grant.setPermissionId(request.getPermissionId());
        grant.setDataClassId(request.getDataClassId());
        grant.setMaskingPolicyId(
                request.getMaskingPolicyId()
        );
        grant.setDecision(request.getDecision());
        grant.setPriority(request.getPriority());

        grant.setValidFrom(
                request.getValidFrom() != null
                        ? request.getValidFrom()
                        : OffsetDateTime.now()
        );

        grant.setValidTo(
                request.getValidTo()
        );

        RolePermissionGrant savedGrant =
                grantRepository.saveAndFlush(grant);

        casbinPolicySyncService.syncRole(
                tenantId,
                grant.getRoleId()
        );

        authorizationCache.invalidateAll();

        return savedGrant;
    }
    @Transactional
    public void deleteGrant(UUID tenantId, UUID grantId) {

        RolePermissionGrant grant =
                grantRepository.findById(grantId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Grant bulunamadı"
                                )
                        );

        if (!grant.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException(
                    "Bu grant bu tenant'a ait değil"
            );
        }

        UUID roleId = grant.getRoleId();

        grantRepository.delete(grant);
        grantRepository.flush();

        casbinPolicySyncService.syncRole(
                tenantId,
                roleId
        );

        authorizationCache.invalidateAll();
    }
    public List<RolePermissionGrant> getGrants(UUID tenantId) {
        return grantRepository.findByTenantId(tenantId);
    }
    public List<RolePermissionGrantResponse> getGrantResponses(
            UUID tenantId
    ) {

        return grantRepository
                .findByTenantId(tenantId)
                .stream()
                .map(grant -> {

                    Role role = roleRepository
                            .findById(grant.getRoleId())
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "Role bulunamadı"
                                    )
                            );

                    Permission permission = permissionRepository
                            .findById(grant.getPermissionId())
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "Permission bulunamadı"
                                    )
                            );

                    return new RolePermissionGrantResponse(
                            role.getCode(),
                            role.getName(),
                            permission.getResourceKey(),
                            permission.getActionKey(),
                            grant.getDecision(),
                            grant.getPriority()
                    );
                })
                .toList();
    }
}