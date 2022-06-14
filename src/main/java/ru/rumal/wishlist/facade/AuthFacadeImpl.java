package ru.rumal.wishlist.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.rumal.wishlist.exception.BadRequestException;
import ru.rumal.wishlist.model.RegistrationRequest;
import ru.rumal.wishlist.model.RegistrationResponse;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.service.UserService;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthFacadeImpl implements AuthFacade {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public RegistrationResponse save(RegistrationRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setEnable(true);
        user.setPicture("");
        userService
                .save(user)
                .orElseThrow(() -> new BadRequestException("Email already exist"));
        return null;
    }
}
