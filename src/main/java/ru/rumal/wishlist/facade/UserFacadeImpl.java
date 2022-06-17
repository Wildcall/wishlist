package ru.rumal.wishlist.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.rumal.wishlist.exception.BadRequestException;
import ru.rumal.wishlist.model.AuthType;
import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.UserDto;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.service.UserService;
import ru.rumal.wishlist.service.impl.DefaultUserExtractor;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final DefaultUserExtractor userExtractor;

    @Override
    public BaseDto getInfo() {
        String email = userExtractor.extractEmail();

        return userService
                .findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Email not found"))
                .toBaseDto();
    }

    @Override
    public String delete() {
        String email = userExtractor.extractEmail();
        User user = userService
                .findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Email not found"));
        userService.delete(user);
        return email;
    }

    @Override
    public BaseDto updateInfo(UserDto userDto) {
        String email = userExtractor.extractEmail();
        return null;
    }

    @Override
    public BaseDto registration(UserDto userDto) {
        User user = (User) userDto.toBaseEntity();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnable(true);
        user.setAuthType(AuthType.APPLICATION);
        return userService
                .save(user)
                .orElseThrow(() -> new BadRequestException("Email already exist"))
                .toBaseDto();
    }
}
