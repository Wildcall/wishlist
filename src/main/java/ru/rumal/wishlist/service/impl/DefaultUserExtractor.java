package ru.rumal.wishlist.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.service.UserExtractor;
import ru.rumal.wishlist.service.UserService;

@Slf4j
@RequiredArgsConstructor
@Service
public class DefaultUserExtractor implements UserExtractor {

    private final UserService userService;

    @Override
    public void extractAndSave(String clientRegistrationId,
                               OidcUser oidcUser) {
        User user = new User();
        if ("google".equals(clientRegistrationId)) {
            user.setEmail(oidcUser.getAttribute("email"));
            user.setName(oidcUser.getAttribute("name"));
            user.setPassword(null);
            user.setPicture(oidcUser.getAttribute("picture"));
            user.setEnable(oidcUser.getAttribute("email_verified"));
            userService.save(user);
        }
    }

    @Override
    public String extractEmail() {
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        if (principal
                .getClass()
                .equals(String.class)) {
            return (String) principal;
        }
        if (principal
                .getClass()
                .equals(DefaultOidcUser.class)) {
            return ((DefaultOidcUser) principal).getAttribute("email");
        }
        return null;
    }
}
