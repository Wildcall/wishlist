package ru.rumal.wishlist.service;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public interface UserExtractor {

    void extract(String clientRegistrationId,
                 OidcUser oidcUser);
}
