package com.demoSec.demoauth.authorization.controller;

import com.demoSec.demoauth.authorization.service.CasbinService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/api/test/casbin")
public class CasbinTestController {

    private final CasbinService casbinService;

    public CasbinTestController(CasbinService casbinService) {
        this.casbinService = casbinService;
    }

    @GetMapping
    public boolean checkPermission(
            @RequestParam String subject,
            @RequestParam String resource,
            @RequestParam String action
    ) {
        return casbinService.checkPermission(
                subject,
                resource,
                action
        );
    }
}