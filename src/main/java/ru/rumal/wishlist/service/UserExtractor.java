package ru.rumal.wishlist.service;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public interface UserExtractor {

    void extractAndSave(String clientRegistrationId,
                        OidcUser oidcUser);

    String extractEmail();
}
