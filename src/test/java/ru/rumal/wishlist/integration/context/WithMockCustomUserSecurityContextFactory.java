package ru.rumal.wishlist.integration.context;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import ru.rumal.wishlist.model.Role;
import ru.rumal.wishlist.model.entity.User;

import java.util.ArrayList;
import java.util.List;

@Getter
public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockAppUser> {

    private final ArrayList<SecurityContext> securityContextArrayList = new ArrayList<>();

    @Override
    public SecurityContext createSecurityContext(WithMockAppUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        User user = new User();
        user.setId(annotation.id());
        user.setEmail(annotation.email());
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role
                                                           .valueOf(annotation.role())
                                                           .name()));

        Authentication auth = new UsernamePasswordAuthenticationToken(user, annotation.password(), authorities);
        context.setAuthentication(auth);
        securityContextArrayList.add(context);
        return context;
    }
}
