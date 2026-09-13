package com.demoSec.demoauth.authorization.service;

import com.demoSec.demoauth.authorization.entity.IdentityProvider;
import com.demoSec.demoauth.authorization.entity.Principal;
import com.demoSec.demoauth.authorization.repository.IdentityProviderRepository;
import com.demoSec.demoauth.authorization.repository.PrincipalRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class PrincipalService {

    private final PrincipalRepository principalRepository;
    private final IdentityProviderRepository identityProviderRepository;

    public PrincipalService(
            PrincipalRepository principalRepository,
            IdentityProviderRepository identityProviderRepository
    ) {
        this.principalRepository = principalRepository;
        this.identityProviderRepository = identityProviderRepository;
    }

    @Transactional
    public Principal syncPrincipal(Jwt jwt) {

        String issuer = jwt.getIssuer().toString();
        String externalKey = jwt.getSubject();

        String username = jwt.getClaimAsString("preferred_username");
        String displayName = jwt.getClaimAsString("name");

        // 1. JWT'nin issuer'ına göre Identity Provider bul
        IdentityProvider identityProvider =
                identityProviderRepository
                        .findByIssuerUri(issuer)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Identity Provider bulunamadı: " + issuer
                                )
                        );

        UUID tenantId = identityProvider.getTenantId();
        // 2. Principal daha önce oluşturulmuş mu?
        Principal principal =
                principalRepository
                        .findByTenantIdAndIdentityProviderIdAndPrincipalTypeAndExternalKey(
                                tenantId,
                                identityProvider.getId(),
                                "USER",
                                externalKey
                        )
                        .orElseGet(Principal::new);

        // 3. Principal bilgilerini doldur
        principal.setTenantId(tenantId);
        principal.setIdentityProviderId(identityProvider.getId());
        principal.setPrincipalType("USER");
        principal.setExternalKey(externalKey);
        principal.setUsernameSnapshot(username);
        principal.setDisplayNameSnapshot(displayName);
        principal.setStatus("ACTIVE");
        principal.setLastSeenAt(OffsetDateTime.now());
        principal.setSynchronizedAt(OffsetDateTime.now());

        return principalRepository.save(principal);
    }
    @Transactional
    public Principal getPrincipalFromJwt(Jwt jwt) {

        String issuer = jwt.getIssuer().toString();
        String externalKey = jwt.getSubject();

        IdentityProvider identityProvider =
                identityProviderRepository
                        .findByIssuerUri(issuer)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Identity Provider bulunamadı: " + issuer
                                )
                        );

        UUID tenantId = identityProvider.getTenantId();

        return principalRepository
                .findByTenantIdAndIdentityProviderIdAndPrincipalTypeAndExternalKey(
                        tenantId,
                        identityProvider.getId(),
                        "USER",
                        externalKey
                )
                .orElseThrow(() ->
                        new IllegalStateException(
                                "JWT kullanıcısı için Principal bulunamadı"
                        )
                );
    }


}