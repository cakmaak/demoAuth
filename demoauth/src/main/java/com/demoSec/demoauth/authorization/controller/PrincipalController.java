package com.demoSec.demoauth.authorization.controller;

import com.demoSec.demoauth.authorization.entity.Principal;
import com.demoSec.demoauth.authorization.service.PrincipalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


@Tag(
        name = "Principals",
        description = "Keycloak principal senkronizasyon işlemleri"
)
@RestController
@RequestMapping("/api/admin/principals")
public class PrincipalController {

    private final PrincipalService principalService;

    public PrincipalController(PrincipalService principalService) {
        this.principalService = principalService;
    }


    @Operation(
            summary = "Keycloak principal'ını senkronize et",
            description = "JWT içerisindeki kullanıcı bilgilerini kullanarak principal kaydını sistem ile senkronize eder."
    )
    @PostMapping("/sync")
    public Principal syncPrincipal(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return principalService.syncPrincipal(jwt);
    }
}