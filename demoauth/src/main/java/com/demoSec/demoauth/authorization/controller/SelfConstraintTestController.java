package com.demoSec.demoauth.authorization.controller;

import com.demoSec.demoauth.authorization.dto.AuthorizationContext;
import com.demoSec.demoauth.authorization.entity.Principal;
import com.demoSec.demoauth.authorization.service.AuthorizationConstraintService;
import com.demoSec.demoauth.authorization.service.PrincipalService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Hidden
@RestController
@RequestMapping("/api/test/constraint")
public class SelfConstraintTestController {

    private final PrincipalService principalService;
    private final AuthorizationConstraintService constraintService;

    public SelfConstraintTestController(
            PrincipalService principalService,
            AuthorizationConstraintService constraintService
    ) {
        this.principalService = principalService;
        this.constraintService = constraintService;
    }

    @GetMapping("/self")
    public boolean testSelf(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam UUID grantId,
            @RequestParam UUID targetPrincipalId
    ) {

        Principal principal =
                principalService.getPrincipalFromJwt(jwt);

        AuthorizationContext context =
                new AuthorizationContext();

        context.setTargetPrincipalId(targetPrincipalId);

        return constraintService.evaluateConstraints(
                principal.getTenantId(),
                grantId,
                principal.getId(),
                context
        );
    }
}