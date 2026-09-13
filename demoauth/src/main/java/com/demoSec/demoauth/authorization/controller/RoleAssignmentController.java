package com.demoSec.demoauth.authorization.controller;

import com.demoSec.demoauth.authorization.dto.CreateRoleAssignmentRequest;
import com.demoSec.demoauth.authorization.entity.RoleAssignment;
import com.demoSec.demoauth.authorization.service.RoleAssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(
        name = "Role Assignments",
        description = "Principal'lara rol atama işlemleri"
)

@RestController
@RequestMapping("/api/admin/role-assignments")
public class RoleAssignmentController {

    private final RoleAssignmentService roleAssignmentService;

    public RoleAssignmentController(
            RoleAssignmentService roleAssignmentService
    ) {
        this.roleAssignmentService = roleAssignmentService;
    }


    @Operation(
            summary = "Principal'a rol ata",
            description = "Belirtilen tenant içerisinde bir principal'a rol ataması oluşturur."
    )
    @PostMapping
    public RoleAssignment assignRole(
            @Parameter(description = "Rol atamasının ait olduğu tenant ID", required = true)
            @RequestParam UUID tenantId,
            @Valid @RequestBody CreateRoleAssignmentRequest request
    ) {

        return roleAssignmentService.assignRole(
                tenantId,
                request
        );
    }
}