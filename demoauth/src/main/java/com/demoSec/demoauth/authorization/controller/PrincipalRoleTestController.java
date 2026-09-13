package com.demoSec.demoauth.authorization.controller;

import com.demoSec.demoauth.authorization.entity.Principal;
import com.demoSec.demoauth.authorization.entity.Role;
import com.demoSec.demoauth.authorization.service.PrincipalRoleService;
import com.demoSec.demoauth.authorization.service.PrincipalService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Hidden
@RestController
@RequestMapping("/api/test/principal-roles")
public class PrincipalRoleTestController {

    private final PrincipalRoleService principalRoleService;
    private final PrincipalService principalService;

    public PrincipalRoleTestController(
            PrincipalRoleService principalRoleService,
            PrincipalService principalService
    ) {
        this.principalRoleService = principalRoleService;
        this.principalService = principalService;
    }

    @GetMapping
    public List<String> getRoles(
            @AuthenticationPrincipal Jwt jwt
    ) {

        Principal principal =
                principalService.getPrincipalFromJwt(jwt);

        List<Role> roles =
                principalRoleService.getRoles(
                        principal.getTenantId(),
                        principal.getId()
                );

        return roles.stream()
                .map(Role::getCode)
                .toList();
    }
}