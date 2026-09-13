package com.demoSec.demoauth.authorization.service;

import com.demoSec.demoauth.authorization.entity.Role;
import com.demoSec.demoauth.authorization.repository.RoleRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CasbinPolicyBootstrapService {

    private final RoleRepository roleRepository;
    private final CasbinPolicySyncService casbinPolicySyncService;

    public CasbinPolicyBootstrapService(
            RoleRepository roleRepository,
            CasbinPolicySyncService casbinPolicySyncService
    ) {
        this.roleRepository = roleRepository;
        this.casbinPolicySyncService = casbinPolicySyncService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void bootstrapCasbinPolicies() {

        System.out.println("CASBIN BOOTSTRAP START");

        List<Role> roles = roleRepository.findAll();

        for (Role role : roles) {

            casbinPolicySyncService.syncRole(
                    role.getTenantId(),
                    role.getId()
            );

            System.out.println(
                    "CASBIN BOOTSTRAP ROLE -> " + role.getCode()
            );
        }

        System.out.println("CASBIN BOOTSTRAP COMPLETE");
    }
}