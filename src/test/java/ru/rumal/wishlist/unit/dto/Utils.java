package ru.rumal.wishlist.unit.dto;

import org.springframework.boot.test.context.TestConfiguration;
import ru.rumal.wishlist.model.AuthType;
import ru.rumal.wishlist.model.Role;
import ru.rumal.wishlist.model.dto.UserDto;

@TestConfiguration
public class Utils {

    public UserDto getUserDto() {
        return new UserDto("id",
                           "email",
                           "password",
                           "new password",
                           "name",
                           "picture",
                           AuthType.APPLICATION,
                           true,
                           Role.USER);
    }
}
