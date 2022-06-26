package ru.rumal.wishlist.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rumal.wishlist.model.AuthType;
import ru.rumal.wishlist.model.Role;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.service.UserExtractor;
import ru.rumal.wishlist.service.UserService;

@Slf4j
@RequiredArgsConstructor
@Service
public class DefaultUserExtractor implements UserExtractor {

    private final UserService userService;

    @Transactional
    @Override
    public void extractAndSave(String clientRegistrationId,
                               OidcUser oidcUser) {
        User user = new User();
        if ("google".equals(clientRegistrationId)) {
            String id = oidcUser.getAttribute("sub");
            User var1 = userService
                    .findById(id)
                    .orElse(null);
            if (var1 != null)
                return;

            String email = oidcUser.getAttribute("email");
            User var2 = userService
                    .findByEmail(email)
                    .orElse(null);
            if (var2 != null) {
                throw new OAuth2AuthenticationException("OAuth2 authentication error!");
            }

            user.setId(id);
            user.setEmail(email);
            user.setPassword(null);
            user.setName(oidcUser.getAttribute("name"));
            user.setPicture(oidcUser.getAttribute("picture"));
            user.setAuthType(AuthType.GOOGLE);
            user.setEnable(oidcUser.getAttribute("email_verified"));
            user.setRole(Role.USER);
            userService.save(user);
        }

    }
}
