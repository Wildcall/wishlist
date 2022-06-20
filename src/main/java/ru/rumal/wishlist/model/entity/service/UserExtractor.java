package ru.rumal.wishlist.model.entity.service;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public interface UserExtractor {

    void extractAndSave(String clientRegistrationId,
                        OidcUser oidcUser);
}
