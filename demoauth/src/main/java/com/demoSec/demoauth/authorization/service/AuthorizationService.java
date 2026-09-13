package com.demoSec.demoauth.authorization.service;

import com.demoSec.demoauth.authorization.dto.AuthorizationContext;
import com.demoSec.demoauth.authorization.entity.Permission;
import com.demoSec.demoauth.authorization.entity.Principal;
import com.demoSec.demoauth.authorization.entity.Role;
import com.demoSec.demoauth.authorization.entity.RolePermissionGrant;
import com.demoSec.demoauth.authorization.repository.PermissionRepository;
import com.demoSec.demoauth.authorization.repository.RolePermissionGrantRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import com.github.benmanes.caffeine.cache.Cache;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;


@Service
public class AuthorizationService {

    private final PrincipalService principalService;
    private final PrincipalRoleService principalRoleService;
    private final CasbinPolicySyncService casbinPolicySyncService;
    private final CasbinService casbinService;
    private final PermissionRepository permissionRepository;
    private final RoleAssignmentScopeService roleAssignmentScopeService;
    private final AuthorizationConstraintService authorizationConstraintService;
    private final RolePermissionGrantRepository rolePermissionGrantRepository;
    private final Cache<String, Boolean> authorizationCache;

    @Value("${authorization.cache.enabled:true}")
    private boolean cacheEnabled;

    public AuthorizationService(
            PrincipalService principalService,
            PrincipalRoleService principalRoleService,
            CasbinPolicySyncService casbinPolicySyncService,
            CasbinService casbinService,
            PermissionRepository permissionRepository,
            RoleAssignmentScopeService roleAssignmentScopeService,
            AuthorizationConstraintService authorizationConstraintService,
            RolePermissionGrantRepository rolePermissionGrantRepository,
            Cache<String, Boolean> authorizationCache
    ) {
        this.principalService = principalService;
        this.principalRoleService = principalRoleService;
        this.casbinPolicySyncService = casbinPolicySyncService;
        this.casbinService = casbinService;
        this.permissionRepository = permissionRepository;
        this.roleAssignmentScopeService = roleAssignmentScopeService;
        this.authorizationConstraintService = authorizationConstraintService;
        this.rolePermissionGrantRepository = rolePermissionGrantRepository;
        this.authorizationCache = authorizationCache;
    }

    private String buildCacheKey(
            UUID tenantId,
            UUID principalId,
            String resource,
            String action,
            String scopeType,
            UUID organizationId,
            UUID regionId,
            AuthorizationContext context
    ) {
        return String.join(":",
                tenantId.toString(),
                principalId.toString(),
                resource,
                action,
                String.valueOf(scopeType),
                String.valueOf(organizationId),
                String.valueOf(regionId),
                String.valueOf(context.getTargetPrincipalId())
        );
    }

    public boolean authorize(
            Jwt jwt,
            String resource,
            String action,
            String scopeType,
            UUID organizationId,
            UUID regionId,
            AuthorizationContext context
    )
    {

        Principal principal =
                principalService.getPrincipalFromJwt(jwt);

        String cacheKey = buildCacheKey(
                principal.getTenantId(),
                principal.getId(),
                resource,
                action,
                scopeType,
                organizationId,
                regionId,
                context
        );

        if (cacheEnabled) {

            Boolean cachedDecision =
                    authorizationCache.getIfPresent(cacheKey);

            if (cachedDecision != null) {
                System.out.println("AUTH CACHE HIT -> " + cachedDecision);
                return cachedDecision;
            }

            System.out.println("AUTH CACHE MISS");

        } else {
            System.out.println("AUTH CACHE DISABLED");
        }

        List<Role> roles =
                principalRoleService.getRoles(
                        principal.getTenantId(),
                        principal.getId()
                );

        Permission permission =
                permissionRepository
                        .findByTenantIdAndResourceKeyAndActionKey(
                                principal.getTenantId(),
                                resource,
                                action
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Permission bulunamadı"
                                )
                        );

        for (Role role : roles) {

            System.out.println(
                    "AUTH CHECK ROLE -> " +
                            role.getCode() +
                            " | " +
                            role.getId()
            );



            // Casbin permission kontrolü
            boolean permissionAllowed =
                    casbinService.checkPermission(
                            role.getCode(),
                            resource,
                            action
                    );

            if (!permissionAllowed) {
                continue;
            }

            // ER11 RolePermissionGrant kayıtlarını getir
            List<RolePermissionGrant> grants =
                    rolePermissionGrantRepository
                            .findByTenantIdAndRoleIdAndPermissionId(
                                    principal.getTenantId(),
                                    role.getId(),
                                    permission.getId()
                            );

            System.out.println(
                    "AUTH ROLE -> " + role.getCode() +
                            " | CASBIN -> " + permissionAllowed +
                            " | GRANT COUNT -> " + grants.size()
            );

            // Aktif grant + priority + DENY/ALLOW kontrolü
            boolean grantAllowed =
                    evaluateGrantDecision(grants);

            if (!grantAllowed) {
                continue;
            }

            // Constraint kontrolü
            for (RolePermissionGrant grant : grants) {

                if (!"ALLOW".equals(grant.getDecision())) {
                    continue;
                }

                boolean constraintsAllowed =
                        authorizationConstraintService.evaluateConstraints(
                                principal.getTenantId(),
                                grant.getId(),
                                principal.getId(),
                                context
                        );

                if (!constraintsAllowed) {
                    continue;
                }


                if (!permission.isScopeRequired()) {

                    if (cacheEnabled) {
                        authorizationCache.put(cacheKey, true);
                    }

                    return true;
                }
                // Scope kontrolü
                boolean scopeAllowed =
                        roleAssignmentScopeService.hasScope(
                                principal.getTenantId(),
                                principal.getId(),
                                role.getId(),
                                scopeType,
                                organizationId,
                                regionId
                        );

                if (scopeAllowed) {

                    System.out.println(
                            "AUTH ALLOW BY ROLE -> " + role.getCode()
                    );

                    if (cacheEnabled) {
                        authorizationCache.put(cacheKey, true);
                    }

                    return true;
                }


            }
        }

        if (cacheEnabled) {
            authorizationCache.put(cacheKey, false);
        }

        return false;
    }

    private boolean evaluateGrantDecision(
            List<RolePermissionGrant> grants
    ) {

        OffsetDateTime now = OffsetDateTime.now();

        // Şu anda aktif olan grant'leri bul
        List<RolePermissionGrant> activeGrants =
                grants.stream()
                        .filter(grant -> {

                            if (grant.getValidFrom() != null
                                    && now.isBefore(grant.getValidFrom())) {
                                return false;
                            }

                            if (grant.getValidTo() != null
                                    && now.isAfter(grant.getValidTo())) {
                                return false;
                            }

                            return true;
                        })
                        .toList();

        if (activeGrants.isEmpty()) {
            return false;
        }

        // En yüksek priority
        int highestPriority =
                activeGrants.stream()
                        .mapToInt(RolePermissionGrant::getPriority)
                        .max()
                        .orElse(Integer.MIN_VALUE);

        // Sadece en yüksek priority'li grant'ler
        List<RolePermissionGrant> highestPriorityGrants =
                activeGrants.stream()
                        .filter(grant ->
                                grant.getPriority() == highestPriority)
                        .toList();

        // En yüksek priority'de DENY varsa DENY kazanır
        boolean denyExists =
                highestPriorityGrants.stream()
                        .anyMatch(grant ->
                                "DENY".equals(grant.getDecision()));

        if (denyExists) {
            return false;
        }


        return highestPriorityGrants.stream()
                .anyMatch(grant ->
                        "ALLOW".equals(grant.getDecision()));
    }

    public List<Permission> getEffectivePermissions(Jwt jwt) {

        Principal principal =
                principalService.getPrincipalFromJwt(jwt);

        UUID tenantId = principal.getTenantId();

        List<Role> roles =
                principalRoleService.getRoles(
                        tenantId,
                        principal.getId()
                );

        List<Permission> permissions = new java.util.ArrayList<>();

        for (Role role : roles) {

            List<RolePermissionGrant> grants =
                    rolePermissionGrantRepository
                            .findByTenantIdAndRoleId(
                                    tenantId,
                                    role.getId()
                            );

            java.util.Set<UUID> allowedPermissionIds =
                    new java.util.HashSet<>();

            java.util.Map<UUID, List<RolePermissionGrant>> grantsByPermission =
                    grants.stream()
                            .collect(java.util.stream.Collectors.groupingBy(
                                    RolePermissionGrant::getPermissionId
                            ));

            for (var entry : grantsByPermission.entrySet()) {

                List<RolePermissionGrant> effectiveGrants =
                        getEffectiveGrants(entry.getValue());

                if (effectiveGrants.isEmpty()) {
                    continue;
                }

                boolean allowed =
                        effectiveGrants.stream()
                                .anyMatch(grant ->
                                        "ALLOW".equals(grant.getDecision()));

                if (allowed) {
                    allowedPermissionIds.add(entry.getKey());
                }
            }

            for (UUID permissionId : allowedPermissionIds) {

                Permission permission =
                        permissionRepository.findById(permissionId)
                                .orElseThrow(() ->
                                        new IllegalArgumentException(
                                                "Permission bulunamadı"
                                        ));

                permissions.add(permission);
            }
        }

        return permissions;
    }
    private List<RolePermissionGrant> getEffectiveGrants(
            List<RolePermissionGrant> grants
    ) {

        OffsetDateTime now = OffsetDateTime.now();

        // 1. Şu anda geçerli grant'leri bul
        List<RolePermissionGrant> activeGrants =
                grants.stream()
                        .filter(grant -> {

                            if (grant.getValidFrom() != null
                                    && now.isBefore(grant.getValidFrom())) {
                                return false;
                            }

                            if (grant.getValidTo() != null
                                    && now.isAfter(grant.getValidTo())) {
                                return false;
                            }

                            return true;
                        })
                        .toList();

        if (activeGrants.isEmpty()) {
            return List.of();
        }

        // 2. En yüksek priority'yi bul
        int highestPriority =
                activeGrants.stream()
                        .mapToInt(RolePermissionGrant::getPriority)
                        .max()
                        .orElse(Integer.MIN_VALUE);

        // 3. Sadece en yüksek priority'li grant'leri al
        List<RolePermissionGrant> highestPriorityGrants =
                activeGrants.stream()
                        .filter(grant ->
                                grant.getPriority() == highestPriority)
                        .toList();

        // 4. En yüksek priority'de DENY varsa izin yok
        boolean denyExists =
                highestPriorityGrants.stream()
                        .anyMatch(grant ->
                                "DENY".equals(grant.getDecision()));

        if (denyExists) {
            return List.of();
        }

        // 5. Sadece ALLOW grant'lerini döndür
        return highestPriorityGrants.stream()
                .filter(grant ->
                        "ALLOW".equals(grant.getDecision()))
                .toList();
    }
}