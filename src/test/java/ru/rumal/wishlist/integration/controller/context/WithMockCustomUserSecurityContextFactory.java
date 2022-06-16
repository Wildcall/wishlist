package ru.rumal.wishlist.integration.controller.context;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.ArrayList;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockAppUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockAppUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Authentication auth =
                new UsernamePasswordAuthenticationToken(annotation.email(), annotation.password(), new ArrayList<>());
        context.setAuthentication(auth);
        return context;
    }
}
