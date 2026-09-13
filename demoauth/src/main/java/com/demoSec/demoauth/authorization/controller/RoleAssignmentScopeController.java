package com.demoSec.demoauth.authorization.controller;

import com.demoSec.demoauth.authorization.dto.CreateRoleAssignmentScopeRequest;
import com.demoSec.demoauth.authorization.entity.RoleAssignmentScope;
import com.demoSec.demoauth.authorization.service.RoleAssignmentScopeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
@Tag(
        name = "Role Assignment Scopes",
        description = "Rol atamalarının TENANT, ORGANIZATION ve REGION kapsamlarını yönetir"
)
@RestController
@RequestMapping("/api/admin/role-assignment-scopes")
public class RoleAssignmentScopeController {

    private final RoleAssignmentScopeService scopeService;

    public RoleAssignmentScopeController(
            RoleAssignmentScopeService scopeService
    ) {
        this.scopeService = scopeService;
    }

    @PostMapping
    public RoleAssignmentScope createScope(
            @RequestParam UUID tenantId,
            @Valid @RequestBody CreateRoleAssignmentScopeRequest request
    ) {

        return scopeService.createScope(
                tenantId,
                request
        );
    }
}