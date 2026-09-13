package com.demoSec.demoauth.authorization.controller;

import com.demoSec.demoauth.authorization.dto.CreateAuthorizationConstraintRequest;
import com.demoSec.demoauth.authorization.entity.AuthorizationConstraint;
import com.demoSec.demoauth.authorization.service.AuthorizationConstraintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(
        name = "Authorization Constraints",
        description = "Permission grant'lerine bağlı authorization constraint'lerini yönetir"
)

@RestController
@RequestMapping("/api/admin/authorization-constraints")
public class AuthorizationConstraintController {

    private final AuthorizationConstraintService constraintService;

    public AuthorizationConstraintController(
            AuthorizationConstraintService constraintService
    ) {
        this.constraintService = constraintService;
    }

    @Operation(
            summary = "Authorization constraint oluştur",
            description = "Bir permission grant'e bağlı authorization constraint tanımlar."
    )
    @PostMapping
    public AuthorizationConstraint createConstraint(
            @Parameter(description = "Constraint'in ait olduğu tenant ID", required = true)
            @RequestParam UUID tenantId,
            @Valid @RequestBody CreateAuthorizationConstraintRequest request
    ) {

        return constraintService.createConstraint(
                tenantId,
                request
        );
    }
}