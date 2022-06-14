package ru.rumal.wishlist.facade;

import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.UserDto;

public interface UserFacade {
    BaseDto getInfo();

    String delete();

    BaseDto updateInfo(UserDto userDto);

    BaseDto registration(UserDto userDto);
}
