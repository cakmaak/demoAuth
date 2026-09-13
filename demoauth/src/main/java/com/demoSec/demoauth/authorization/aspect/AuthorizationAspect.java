
        package com.demoSec.demoauth.authorization.aspect;

import com.demoSec.demoauth.authorization.annotation.Authorize;
import com.demoSec.demoauth.authorization.dto.AuthorizationContext;
import com.demoSec.demoauth.authorization.exception.AuthorizationDeniedException;
import com.demoSec.demoauth.authorization.service.AuthorizationService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
public class AuthorizationAspect {

    private final AuthorizationService authorizationService;
    private final HttpServletRequest request;

    public AuthorizationAspect(
            AuthorizationService authorizationService,
            HttpServletRequest request
    ) {
        this.authorizationService = authorizationService;
        this.request = request;
    }

    @Around("@annotation(authorize)")
    public Object checkAuthorization(
            ProceedingJoinPoint joinPoint,
            Authorize authorize
    ) throws Throwable {

        /*
         * 1. JWT
         */
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !(authentication.getPrincipal() instanceof Jwt jwt)) {

            throw new SecurityException(
                    "Authenticated kullanıcı bulunamadı"
            );
        }

        /*
         * 2. Request'ten authorization context bilgilerini al
         */
        String scopeType =
                request.getParameter("scopeType");

        String organizationIdParam =
                request.getParameter("organizationId");

        String regionIdParam =
                request.getParameter("regionId");

        String targetPrincipalIdParam =
                request.getParameter("targetPrincipalId");

        UUID organizationId =
                organizationIdParam != null
                        ? UUID.fromString(organizationIdParam)
                        : null;

        UUID regionId =
                regionIdParam != null
                        ? UUID.fromString(regionIdParam)
                        : null;

        UUID targetPrincipalId =
                targetPrincipalIdParam != null
                        ? UUID.fromString(targetPrincipalIdParam)
                        : null;

        /*
         * 3. Authorization Context
         */
        AuthorizationContext context =
                new AuthorizationContext();

        context.setScopeType(scopeType);
        context.setOrganizationId(organizationId);
        context.setRegionId(regionId);
        context.setTargetPrincipalId(targetPrincipalId);

        /*
         * 4. Authorization
         */
        boolean allowed =
                authorizationService.authorize(
                        jwt,
                        authorize.resource(),
                        authorize.action(),
                        scopeType,
                        organizationId,
                        regionId,
                        context
                );

        /*
         * 5. DENY
         */
        if (!allowed) {

            throw new AuthorizationDeniedException(
                    "Bu işlem için yetkiniz yok"
            );
        }

        /*
         * 6. ALLOW → controller çalışır
         */
        return joinPoint.proceed();
    }
}

