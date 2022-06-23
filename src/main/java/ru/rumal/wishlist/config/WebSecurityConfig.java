package ru.rumal.wishlist.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.rumal.wishlist.model.Role;
import ru.rumal.wishlist.service.UserExtractor;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    private final UserExtractor userExtractor;
    private final AuthenticationEntryPoint customAuthenticationEntryPoint;
    private final AccessDeniedHandler customAccessDeniedHandler;
    private final UsernamePasswordAuthenticationFilter customAuthenticationFilter;

    @Value("${spring.profile.active:prod}")
    private String profile;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        customAuthenticationFilter.setFilterProcessesUrl("/api/v1/auth/login");
        //  @formatter:off
        if ("dev".equals(profile))
            http.csrf().disable();
        return http
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeRequests(r -> r
                        .antMatchers("/js/*", "/assets/*").permitAll()
                        .antMatchers(HttpMethod.GET, "/favicon.ico").permitAll()
                        .antMatchers(HttpMethod.GET, "/").permitAll()
                        .antMatchers(HttpMethod.GET, "/error").permitAll()
                        .antMatchers(HttpMethod.POST, "/api/v1/user/registration").permitAll()
                        .antMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                        .antMatchers(HttpMethod.GET, "/api/v1/auth/logout").permitAll()
                        .antMatchers("/api/v1/basic_event", "/api/v1/basic_event/*").hasRole(Role.ADMIN.name())
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true))
                .exceptionHandling()
                    .authenticationEntryPoint(customAuthenticationEntryPoint)
                    .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/")
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(this.oidcUserService())))
                .addFilter(customAuthenticationFilter)
                .logout(l -> l
                        .logoutUrl("/api/v1/auth/logout")
                        .logoutSuccessUrl("/")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())
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
            userExtractor.extractAndSave(clientRegistrationId, oidcUser);
            return oidcUser;
        };
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }
}
