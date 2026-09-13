package com.demoSec.demoauth.authorization.controller;

import com.demoSec.demoauth.authorization.dto.CreatePermissionRequest;
import com.demoSec.demoauth.authorization.entity.Permission;
import com.demoSec.demoauth.authorization.service.PermissionService;
import com.demoSec.demoauth.authorization.service.PrincipalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@Tag(
        name = "Permissions",
        description = "Resource ve action tabanlı permission kataloğu"
)
@RestController
@RequestMapping("/api/admin/permissions")
public class PermissionController {

    private final PermissionService permissionService;
    private final PrincipalService principalService;

    public PermissionController(
            PermissionService permissionService,
            PrincipalService principalService
    ) {
        this.permissionService = permissionService;
        this.principalService = principalService;
    }

    @Operation(
            summary = "Yeni permission oluştur",
            description = "Resource ve action tabanlı yeni bir permission tanımlar."
    )
    @PostMapping
    public Permission createPermission(

            @Parameter(description = "Permission'ın oluşturulacağı tenant ID", required = true)
            @RequestParam UUID tenantId,
            @Valid @RequestBody CreatePermissionRequest request
    ) {

        return permissionService.createPermission(
                tenantId,
                request
        );
    }


    @Operation(
            summary = "Permission'ları listele",
            description = "Oturum açmış principal'ın tenant'ına ait permission kataloğunu listeler."
    )
    @GetMapping
    public List<Permission> getPermissions(
            @AuthenticationPrincipal Jwt jwt
    ) {

        UUID tenantId =
                principalService
                        .getPrincipalFromJwt(jwt)
                        .getTenantId();

        return permissionService.getPermissions(tenantId);
    }
}