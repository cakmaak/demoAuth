package com.demoSec.demoauth.authorization.controller;

import com.demoSec.demoauth.authorization.dto.CreateRoleRequest;
import com.demoSec.demoauth.authorization.entity.Role;
import com.demoSec.demoauth.authorization.service.PrincipalService;
import com.demoSec.demoauth.authorization.service.RoleService;
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
        name = "Roles",
        description = "Rol tanımlama ve listeleme işlemleri"
)



@RestController
@RequestMapping("/api/admin/roles")
public class RoleController {

    private final RoleService roleService;
    private final PrincipalService principalService;

    public RoleController(
            RoleService roleService,
            PrincipalService principalService
    ) {
        this.roleService = roleService;
        this.principalService = principalService;
    }

    @Operation(
            summary = "Yeni rol oluştur",
            description = "Belirtilen tenant için yeni bir rol oluşturur."
    )

    @PostMapping
    public Role createRole(

            @Parameter(description = "Rolün oluşturulacağı tenant ID", required = true)
            @RequestParam UUID tenantId,
            @Valid @RequestBody CreateRoleRequest request
    ) {

        return roleService.createRole(
                tenantId,
                request
        );
    }

    @Operation(
            summary = "Rolleri listele",
            description = "Oturum açmış principal'ın tenant'ına ait rolleri listeler."
    )
    @GetMapping
    public List<Role> getRoles(
            @AuthenticationPrincipal Jwt jwt
    ) {

        UUID tenantId =
                principalService
                        .getPrincipalFromJwt(jwt)
                        .getTenantId();

        return roleService.getRoles(tenantId);
    }
}