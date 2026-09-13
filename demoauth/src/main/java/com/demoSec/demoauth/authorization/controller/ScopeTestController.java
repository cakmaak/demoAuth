package com.demoSec.demoauth.authorization.controller;

import com.demoSec.demoauth.authorization.entity.Principal;
import com.demoSec.demoauth.authorization.entity.Role;
import com.demoSec.demoauth.authorization.service.PrincipalRoleService;
import com.demoSec.demoauth.authorization.service.PrincipalService;
import com.demoSec.demoauth.authorization.service.RoleAssignmentScopeService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Hidden
@RestController
@RequestMapping("/api/test/scope")
public class ScopeTestController {

    private final PrincipalService principalService;
    private final PrincipalRoleService principalRoleService;
    private final RoleAssignmentScopeService scopeService;

    public ScopeTestController(
            PrincipalService principalService,
            PrincipalRoleService principalRoleService,
            RoleAssignmentScopeService scopeService
    ) {
        this.principalService = principalService;
        this.principalRoleService = principalRoleService;
        this.scopeService = scopeService;
    }

    @GetMapping
    public boolean checkScope(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam String scopeType,
            @RequestParam(required = false) UUID organizationId,
            @RequestParam(required = false) UUID regionId
    ) {

        Principal principal =
                principalService.getPrincipalFromJwt(jwt);

        List<Role> roles =
                principalRoleService.getRoles(
                        principal.getTenantId(),
                        principal.getId()
                );

        for (Role role : roles) {

            boolean allowed =
                    scopeService.hasScope(
                            principal.getTenantId(),
                            principal.getId(),
                            role.getId(),
                            scopeType,
                            organizationId,
                            regionId
                    );

            if (allowed) {
                return true;
            }
        }

        return false;
    }
}