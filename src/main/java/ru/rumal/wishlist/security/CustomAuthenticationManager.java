package ru.rumal.wishlist.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.rumal.wishlist.service.UserService;

import java.util.ArrayList;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomAuthenticationManager implements AuthenticationManager {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication
                .getCredentials()
                .toString();

        UserDetails user = userService.loadUserByUsername(email);

        if (user.getPassword() != null
                && passwordEncoder.matches(password, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(user, password, new ArrayList<>());
        }

        throw new BadCredentialsException("Bad credentials");
    }
}
