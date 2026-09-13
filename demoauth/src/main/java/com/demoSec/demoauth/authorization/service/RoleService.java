package com.demoSec.demoauth.authorization.service;

import com.demoSec.demoauth.authorization.dto.CreateRoleRequest;
import com.demoSec.demoauth.authorization.entity.Role;
import com.demoSec.demoauth.authorization.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    public Role createRole(
            UUID tenantId,
            CreateRoleRequest request
    ) {

        // Aynı tenant içerisinde aynı code var mı?
        if (roleRepository
                .findByTenantIdAndCode(
                        tenantId,
                        request.getCode()
                )
                .isPresent()) {

            throw new IllegalStateException(
                    "Bu tenant içerisinde bu role zaten mevcut: "
                            + request.getCode()
            );
        }

        Role role = new Role();

        role.setTenantId(tenantId);
        role.setCode(request.getCode());
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        role.setSystem(request.isSystem());
        role.setActive(true);

        return roleRepository.save(role);
    }

    public List<Role> getRoles(UUID tenantId) {

        return roleRepository.findByTenantId(tenantId);
    }
}