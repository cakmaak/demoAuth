package com.demoSec.demoauth.authorization.controller;

import com.demoSec.demoauth.authorization.service.CasbinPolicySyncService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Hidden
@RestController
@RequestMapping("/api/test/casbin")
public class CasbinPolicySyncController {

    private final CasbinPolicySyncService syncService;

    public CasbinPolicySyncController(
            CasbinPolicySyncService syncService
    ) {
        this.syncService = syncService;
    }

    @PostMapping("/sync/{roleId}")
    public String syncRole(
            @RequestParam UUID tenantId,
            @PathVariable UUID roleId
    ) {

        syncService.syncRole(tenantId, roleId);

        return "Role Casbin'e sync edildi";
    }
}