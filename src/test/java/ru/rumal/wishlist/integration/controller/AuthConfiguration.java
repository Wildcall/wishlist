package ru.rumal.wishlist.integration.controller;

import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Configuration
class AuthConfiguration {

    private final String username = "test@test.com";
    private final String password = "testtest";

    @Bean
    public UserDetailsService inMemoryUserServiceForTest(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return auth
                .inMemoryAuthentication()
                .passwordEncoder(passwordEncoder)
                .withUser(username)
                .password(password)
                .roles("USER")
                .and()
                .getUserDetailsService();

    }
}