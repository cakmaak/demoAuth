package com.demoSec.demoauth.authorization.controller;

import com.demoSec.demoauth.authorization.dto.EffectivePermissionResponse;
import com.demoSec.demoauth.authorization.entity.Permission;
import com.demoSec.demoauth.authorization.entity.Principal;
import com.demoSec.demoauth.authorization.service.AuthorizationService;
import com.demoSec.demoauth.authorization.service.PrincipalRoleService;
import com.demoSec.demoauth.authorization.service.PrincipalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "Authorization",
        description = "Oturum açmış principal'ın efektif yetkileri"
)

@RestController
@RequestMapping("/api/authorization")
public class AuthorizationController {

    private final PrincipalService principalService;
    private final PrincipalRoleService principalRoleService;
    private final AuthorizationService authorizationService;

    public AuthorizationController(
            PrincipalService principalService,
            PrincipalRoleService principalRoleService,
            AuthorizationService autharizationService
    ) {
        this.principalService = principalService;
        this.principalRoleService = principalRoleService;
        this.authorizationService=autharizationService;
    }

    @Operation(
            summary = "Efektif permission'larımı getir",
            description = "Oturum açmış principal'ın rol ve grant ilişkilerinden elde edilen efektif permission'larını döndürür."
    )
    @GetMapping("/me")
    public List<EffectivePermissionResponse> getMyPermissions(
            @AuthenticationPrincipal Jwt jwt
    ) {

        List<Permission> permissions =
                authorizationService.getEffectivePermissions(jwt);

        return permissions.stream()
                .map(permission ->
                        new EffectivePermissionResponse(
                                permission.getResourceKey(),
                                permission.getActionKey(),
                                true
                        )
                )
                .toList();
    }
}