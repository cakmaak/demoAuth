package com.demoSec.demoauth.authorization.controller;

import com.demoSec.demoauth.authorization.dto.CreateRolePermissionGrantRequest;
import com.demoSec.demoauth.authorization.dto.RolePermissionGrantResponse;
import com.demoSec.demoauth.authorization.entity.Principal;
import com.demoSec.demoauth.authorization.entity.RolePermissionGrant;
import com.demoSec.demoauth.authorization.service.PrincipalService;
import com.demoSec.demoauth.authorization.service.RolePermissionGrantService;
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
        name = "Role Permission Grants",
        description = "Rollere permission ALLOW/DENY grant atama işlemleri"
)

@RestController
@RequestMapping("/api/admin/role-permission-grants")
public class RolePermissionGrantController {

    private final RolePermissionGrantService grantService;
    private final PrincipalService principalService;

    public RolePermissionGrantController(
            RolePermissionGrantService grantService,
            PrincipalService principalService
    ) {
        this.grantService = grantService;
        this.principalService = principalService;
    }

    @Operation(
            summary = "Role permission grant oluştur",
            description = "Bir role permission için ALLOW veya DENY grant tanımlar."
    )
    @PostMapping
    public RolePermissionGrant createGrant(
            @Parameter(description = "Grant'ın ait olduğu tenant ID", required = true)
            @RequestParam UUID tenantId,
            @Valid @RequestBody CreateRolePermissionGrantRequest request
    ) {

        return grantService.createGrant(
                tenantId,
                request
        );
    }

    @Operation(
            summary = "Role permission grant'larını listele",
            description = "Oturum açmış principal'ın tenant'ındaki role-permission grant'larını listeler."
    )

    @GetMapping
    public List<RolePermissionGrantResponse> getGrants(
            @AuthenticationPrincipal Jwt jwt
    ) {

        UUID tenantId =
                principalService
                        .getPrincipalFromJwt(jwt)
                        .getTenantId();

        return grantService.getGrantResponses(tenantId);
    }
    @Operation(
            summary = "Role permission grant sil",
            description = "Belirtilen grant kaydını siler. Yetki değişikliği Casbin'e senkronize edilir ve authorization cache temizlenir."
    )
    @DeleteMapping("/{grantId}")
    public void deleteGrant(
            @Parameter(description = "Silinecek grant ID", required = true)
            @PathVariable UUID grantId,
            @AuthenticationPrincipal Jwt jwt
    ) {

        Principal principal =
                principalService.getPrincipalFromJwt(jwt);

        grantService.deleteGrant(
                principal.getTenantId(),
                grantId
        );
    }
}