package ru.rumal.wishlist.integration.context;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.ArrayList;

@Getter
public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockAppUser> {

    private final ArrayList<SecurityContext> securityContextArrayList = new ArrayList<>();

    @Override
    public SecurityContext createSecurityContext(WithMockAppUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Authentication auth =
                new UsernamePasswordAuthenticationToken(annotation.email(), annotation.password(), new ArrayList<>());
        context.setAuthentication(auth);
        securityContextArrayList.add(context);
        return context;
    }
}
