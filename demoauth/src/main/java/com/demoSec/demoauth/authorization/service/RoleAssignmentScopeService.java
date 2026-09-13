package com.demoSec.demoauth.authorization.service;

import com.demoSec.demoauth.authorization.dto.CreateRoleAssignmentScopeRequest;
import com.demoSec.demoauth.authorization.entity.RoleAssignment;
import com.demoSec.demoauth.authorization.entity.RoleAssignmentScope;
import com.demoSec.demoauth.authorization.entity.RoleInheritance;
import com.demoSec.demoauth.authorization.repository.RoleAssignmentRepository;
import com.demoSec.demoauth.authorization.repository.RoleAssignmentScopeRepository;
import com.demoSec.demoauth.authorization.repository.RoleInheritanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class RoleAssignmentScopeService {

    private final RoleAssignmentScopeRepository scopeRepository;
    private final RoleAssignmentRepository roleAssignmentRepository;
    private final RoleInheritanceRepository roleInheritanceRepository;

    public RoleAssignmentScopeService(
            RoleAssignmentScopeRepository scopeRepository,
            RoleAssignmentRepository roleAssignmentRepository,
            RoleInheritanceRepository roleInheritanceRepository
    ) {
        this.scopeRepository = scopeRepository;
        this.roleAssignmentRepository = roleAssignmentRepository;
        this.roleInheritanceRepository = roleInheritanceRepository;
    }

    @Transactional
    public RoleAssignmentScope createScope(
            UUID tenantId,
            CreateRoleAssignmentScopeRequest request
    ) {

        RoleAssignment assignment =
                roleAssignmentRepository
                        .findById(request.getRoleAssignmentId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Role assignment bulunamadı"
                                )
                        );

        if (!assignment.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException(
                    "Role assignment bu tenant'a ait değil"
            );
        }

        String scopeType = request.getScopeType();

        if (!scopeType.equals("TENANT")
                && !scopeType.equals("ORGANIZATION")
                && !scopeType.equals("REGION")) {

            throw new IllegalArgumentException(
                    "Geçersiz scope type"
            );
        }

        if (scopeType.equals("TENANT")) {

            if (request.getOrganizationId() != null
                    || request.getRegionId() != null) {

                throw new IllegalArgumentException(
                        "TENANT scope için organizationId ve regionId NULL olmalıdır"
                );
            }
        }

        if (scopeType.equals("ORGANIZATION")) {

            if (request.getOrganizationId() == null
                    || request.getRegionId() != null) {

                throw new IllegalArgumentException(
                        "ORGANIZATION scope için sadece organizationId dolu olmalıdır"
                );
            }
        }

        if (scopeType.equals("REGION")) {

            if (request.getOrganizationId() != null
                    || request.getRegionId() == null) {

                throw new IllegalArgumentException(
                        "REGION scope için sadece regionId dolu olmalıdır"
                );
            }
        }

        if (!request.getDecision().equals("INCLUDE")
                && !request.getDecision().equals("EXCLUDE")) {

            throw new IllegalArgumentException(
                    "Decision sadece INCLUDE veya EXCLUDE olabilir"
            );
        }

        RoleAssignmentScope scope =
                new RoleAssignmentScope();

        scope.setTenantId(tenantId);
        scope.setRoleAssignmentId(
                request.getRoleAssignmentId()
        );
        scope.setScopeType(scopeType);
        scope.setOrganizationId(
                request.getOrganizationId()
        );
        scope.setRegionId(
                request.getRegionId()
        );
        scope.setDecision(
                request.getDecision()
        );
        scope.setIncludeDescendants(
                request.isIncludeDescendants()
        );

        scope.setValidFrom(
                request.getValidFrom() != null
                        ? request.getValidFrom()
                        : OffsetDateTime.now()
        );

        scope.setValidTo(
                request.getValidTo()
        );

        return scopeRepository.save(scope);
    }
    public boolean hasScope(
            UUID tenantId,
            UUID principalId,
            UUID roleId,
            String scopeType,
            UUID organizationId,
            UUID regionId
    ) {

        Set<UUID> visitedRoles = new HashSet<>();

        return hasScopeRecursive(
                tenantId,
                principalId,
                roleId,
                scopeType,
                organizationId,
                regionId,
                visitedRoles
        );
    }
    private boolean hasScopeRecursive(
            UUID tenantId,
            UUID principalId,
            UUID roleId,
            String scopeType,
            UUID organizationId,
            UUID regionId,
            Set<UUID> visitedRoles
    ) {

        // Döngü kontrolü
        if (!visitedRoles.add(roleId)) {
            return false;
        }

        System.out.println("========== ROLE SCOPE DEBUG ==========");
        System.out.println("Checking role = " + roleId);

        /*
         * 1. Önce bu role ait aktif assignment'ları kontrol et
         */
        List<RoleAssignment> assignments =
                roleAssignmentRepository
                        .findByTenantIdAndPrincipalIdAndRoleIdAndRevokedAtIsNull(
                                tenantId,
                                principalId,
                                roleId
                        );

        OffsetDateTime now = OffsetDateTime.now();

        for (RoleAssignment assignment : assignments) {

            // Role assignment zaman kontrolü
            if (assignment.getValidFrom() != null
                    && now.isBefore(assignment.getValidFrom())) {
                continue;
            }

            if (assignment.getValidTo() != null
                    && now.isAfter(assignment.getValidTo())) {
                continue;
            }

            List<RoleAssignmentScope> scopes =
                    scopeRepository
                            .findByTenantIdAndRoleAssignmentId(
                                    tenantId,
                                    assignment.getId()
                            );

            boolean included = false;

            for (RoleAssignmentScope scope : scopes) {

                System.out.println("========== SCOPE DEBUG ==========");
                System.out.println("Role       = " + roleId);
                System.out.println("Assignment = " + assignment.getId());
                System.out.println("Scope ID   = " + scope.getId());
                System.out.println("Decision   = " + scope.getDecision());
                System.out.println("Scope Type = " + scope.getScopeType());
                System.out.println("Region ID  = " + scope.getRegionId());
                System.out.println("Valid From = " + scope.getValidFrom());
                System.out.println("Valid To   = " + scope.getValidTo());

                // Scope zaman kontrolü
                if (scope.getValidFrom() != null
                        && now.isBefore(scope.getValidFrom())) {

                    System.out.println(
                            "RESULT = SKIP (valid_from gelecekte)"
                    );
                    System.out.println("================================");

                    continue;
                }

                if (scope.getValidTo() != null
                        && now.isAfter(scope.getValidTo())) {

                    System.out.println(
                            "RESULT = SKIP (valid_to geçmişte)"
                    );
                    System.out.println("================================");

                    continue;
                }

                // Scope type kontrolü
                if (!scope.getScopeType().equals(scopeType)) {
                    continue;
                }

                boolean matches = false;

                if ("TENANT".equals(scopeType)) {

                    matches = true;

                } else if ("ORGANIZATION".equals(scopeType)
                        && organizationId != null
                        && organizationId.equals(scope.getOrganizationId())) {

                    matches = true;

                } else if ("REGION".equals(scopeType)
                        && regionId != null
                        && regionId.equals(scope.getRegionId())) {

                    matches = true;
                }

                if (!matches) {
                    continue;
                }

                // EXCLUDE
                if ("EXCLUDE".equals(scope.getDecision())) {

                    System.out.println(
                            "RESULT = FALSE (EXCLUDE bulundu)"
                    );
                    System.out.println("================================");

                    return false;
                }

                // INCLUDE
                if ("INCLUDE".equals(scope.getDecision())) {
                    included = true;
                }
            }

            if (included) {

                System.out.println(
                        "RESULT = TRUE (INCLUDE bulundu)"
                );
                System.out.println("================================");

                return true;
            }
        }

        /*
         * 2. Bu role inherited edilmiş bir rol mü?
         *
         * Bizim modelimiz:
         *
         * parent_role_id = miras veren / üst rol
         * child_role_id  = miras alınan rol
         *
         * Örneğin:
         *
         * FLEET_ceo
         *      ↓
         * FLEET_MANAGER
         *
         * FLEET_ceo'nun scope'u,
         * FLEET_MANAGER yetkisi için kullanılabilir.
         */
        List<RoleInheritance> inheritances =
                roleInheritanceRepository
                        .findByTenantIdAndChildRoleIdAndActiveTrue(
                                tenantId,
                                roleId
                        );

        for (RoleInheritance inheritance : inheritances) {

            UUID parentRoleId =
                    inheritance.getParentRoleId();

            System.out.println(
                    "Inheritance bulundu: "
                            + parentRoleId
                            + " -> "
                            + roleId
            );

            boolean inheritedScope =
                    hasScopeRecursive(
                            tenantId,
                            principalId,
                            parentRoleId,
                            scopeType,
                            organizationId,
                            regionId,
                            visitedRoles
                    );

            if (inheritedScope) {
                return true;
            }
        }

        return false;
    }
}