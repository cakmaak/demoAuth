package com.demoSec.demoauth.authorization.controller;

import com.demoSec.demoauth.authorization.dto.PermissionMatrixResponse;
import com.demoSec.demoauth.authorization.entity.Principal;
import com.demoSec.demoauth.authorization.service.PermissionMatrixService;
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
        name = "Permission Matrix",
        description = "Rol ve permission ilişkilerinin matrix görünümü"
)

@RestController
@RequestMapping("/api/admin/permission-matrix")
public class PermissionMatrixController {

    private final PermissionMatrixService permissionMatrixService;
    private final PrincipalService principalService;

    public PermissionMatrixController(
            PermissionMatrixService permissionMatrixService,
            PrincipalService principalService
    ) {
        this.permissionMatrixService = permissionMatrixService;
        this.principalService = principalService;
    }


    @Operation(
            summary = "Permission Matrix'i getir",
            description = "Tenant içerisindeki rollerin doğrudan ve inheritance yoluyla sahip olduğu permission'ları matrix olarak döndürür."
    )
    @GetMapping
    public List<PermissionMatrixResponse> getMatrix(
            @AuthenticationPrincipal Jwt jwt
    ) {

        Principal principal =
                principalService.getPrincipalFromJwt(jwt);

        return permissionMatrixService.getMatrix(
                principal.getTenantId()
        );
    }
}