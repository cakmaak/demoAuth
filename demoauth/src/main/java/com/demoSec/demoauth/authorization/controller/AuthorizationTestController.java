package com.demoSec.demoauth.authorization.controller;

import com.demoSec.demoauth.authorization.annotation.Authorize;
import com.demoSec.demoauth.authorization.dto.AuthorizationContext;
import com.demoSec.demoauth.authorization.service.AuthorizationService;
import com.demoSec.demoauth.authorization.service.CasbinService;
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
@RequestMapping("/api/test/authorization")
public class AuthorizationTestController {

    private final AuthorizationService authorizationService;
    private final CasbinService casbinService;

    public AuthorizationTestController(
            AuthorizationService authorizationService,
            CasbinService casbinService
    ) {
        this.authorizationService = authorizationService;
        this.casbinService=casbinService;
    }

    @Authorize(
            resource = "fleet.vehicle",
            action = "VIEW"
    )

    @GetMapping
    public boolean authorize(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam String resource,
            @RequestParam String action,
            @RequestParam String scopeType,
            @RequestParam(required = false) UUID organizationId,
            @RequestParam(required = false) UUID regionId,
            @RequestParam UUID targetPrincipalId
    ) {
        return true;
    }
    @GetMapping("/casbin/policies")
    public List<List<String>> getCasbinPolicies() {
        return casbinService.getPolicies();
    }
}