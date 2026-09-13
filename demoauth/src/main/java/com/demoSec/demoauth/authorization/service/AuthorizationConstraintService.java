package com.demoSec.demoauth.authorization.service;

import com.demoSec.demoauth.authorization.dto.CreateAuthorizationConstraintRequest;
import com.demoSec.demoauth.authorization.entity.AuthorizationConstraint;
import com.demoSec.demoauth.authorization.entity.RolePermissionGrant;
import com.demoSec.demoauth.authorization.repository.AuthorizationConstraintRepository;
import com.demoSec.demoauth.authorization.repository.RolePermissionGrantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.demoSec.demoauth.authorization.dto.AuthorizationContext;

import java.util.UUID;

@Service
public class AuthorizationConstraintService {

    private final AuthorizationConstraintRepository constraintRepository;
    private final RolePermissionGrantRepository grantRepository;

    public AuthorizationConstraintService(
            AuthorizationConstraintRepository constraintRepository,
            RolePermissionGrantRepository grantRepository
    ) {
        this.constraintRepository = constraintRepository;
        this.grantRepository = grantRepository;
    }

    @Transactional
    public AuthorizationConstraint createConstraint(
            UUID tenantId,
            CreateAuthorizationConstraintRequest request
    ) {

        RolePermissionGrant grant =
                grantRepository
                        .findById(request.getRolePermissionGrantId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Role permission grant bulunamadı"
                                )
                        );

        if (!grant.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException(
                    "Role permission grant bu tenant'a ait değil"
            );
        }

        String constraintKey = request.getConstraintKey();

        if (!constraintKey.equals("SELF")
                && !constraintKey.equals("ASSIGNED")
                && !constraintKey.equals("OWNER_ORG")
                && !constraintKey.equals("WORKFLOW_ACTOR")) {

            throw new IllegalArgumentException(
                    "Geçersiz constraint key"
            );
        }

        AuthorizationConstraint constraint =
                new AuthorizationConstraint();

        constraint.setTenantId(tenantId);
        constraint.setRolePermissionGrantId(
                request.getRolePermissionGrantId()
        );
        constraint.setConstraintKey(
                constraintKey
        );
        constraint.setParameters(
                request.getParameters()
        );
        constraint.setRequired(
                request.isRequired()
        );

        return constraintRepository.save(constraint);
    }
    public boolean evaluateConstraints(
            UUID tenantId,
            UUID grantId,
            UUID currentPrincipalId,
            AuthorizationContext context
    ) {

        var constraints =
                constraintRepository
                        .findByTenantIdAndRolePermissionGrantId(
                                tenantId,
                                grantId
                        );

        for (AuthorizationConstraint constraint : constraints) {

            // Opsiyonel constraint ise ve uygulanmayacaksa geç
            if (!constraint.isRequired()) {
                continue;
            }

            switch (constraint.getConstraintKey()) {

                case "SELF":

                    if (context.getTargetPrincipalId() == null) {
                        return false;
                    }

                    if (!currentPrincipalId.equals(
                            context.getTargetPrincipalId())) {

                        return false;
                    }

                    break;

                default:

                    throw new IllegalArgumentException(
                            "Desteklenmeyen constraint: "
                                    + constraint.getConstraintKey()
                    );
            }
        }

        return true;
    }
}