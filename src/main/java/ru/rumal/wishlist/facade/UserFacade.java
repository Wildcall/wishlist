package ru.rumal.wishlist.facade;

import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.UserDto;

import java.security.Principal;

public interface UserFacade {
    BaseDto registration(UserDto userDto);

    BaseDto getInfo(Principal principal);

    String delete(Principal principal);

    BaseDto updateInfo(Principal principal,
                       UserDto userDto);

    BaseDto updatePassword(Principal principal,
                           UserDto userDto);
}
