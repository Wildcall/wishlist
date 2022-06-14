package ru.rumal.wishlist.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import ru.rumal.wishlist.exception.ApiError;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager,
                                      ObjectMapper objectMapper) {
        super(authenticationManager);
        this.objectMapper = objectMapper;
        super.setUsernameParameter("email");
        super.setPasswordParameter("password");
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());
        log.info("Success login! Email: {}", request.getParameter("email"));
        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        String email = request.getParameter("email");
        log.info("Failed login! Email: {} / Cause: {}", email, failed.getMessage());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Email or password is invalid");
        ApiError.writeApiError(response, objectMapper, apiError);
    }
}
