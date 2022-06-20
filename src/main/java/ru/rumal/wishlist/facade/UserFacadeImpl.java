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
import ru.rumal.wishlist.model.entity.service.UserService;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public BaseDto registration(UserDto userDto) {
        User user = (User) userDto.toBaseEntity();
        user.setId(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnable(true);
        user.setAuthType(AuthType.APPLICATION);

        return userService
                .save(user)
                .orElseThrow(() -> new BadRequestException("Email already exist"))
                .toBaseDto();
    }

    @Override
    public BaseDto getInfo(Principal principal) {
        String id = principal.getName();

        return userService
                .findById(id)
                .orElseThrow(() -> new BadRequestException("User not found"))
                .toBaseDto();
    }

    @Override
    public String delete(Principal principal) {
        String id = principal.getName();

        if (userService.delete(id)) return id;
        throw new BadRequestException("User not found");
    }

    @Override
    public BaseDto updateInfo(Principal principal,
                              UserDto userDto) {
        throw new BadRequestException("Not impl");
    }

    @Override
    public BaseDto updatePassword(Principal principal,
                                  UserDto userDto) {
        throw new BadRequestException("Not impl");
    }
}
