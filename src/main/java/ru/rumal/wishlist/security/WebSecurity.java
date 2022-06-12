package ru.rumal.wishlist.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import ru.rumal.wishlist.service.UserExtractor;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurity {

    private final UserExtractor userExtractor;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //  @formatter:off
        return http
                .csrf().disable()
                .authorizeRequests(r -> r
                        .antMatchers("/").permitAll()
                        .anyRequest().authenticated())
                .logout(l -> l
                        .logoutSuccessUrl("/").permitAll()
                        .invalidateHttpSession(true))
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(this.oidcUserService())))
                .build();
        //  @formatter:on
    }

    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        final OidcUserService delegate = new OidcUserService();

        return (userRequest) -> {
            String clientRegistrationId = userRequest
                    .getClientRegistration()
                    .getRegistrationId();
            OidcUser oidcUser = delegate.loadUser(userRequest);
            userExtractor.extract(clientRegistrationId, oidcUser);
            return oidcUser;
        };
    }
}
