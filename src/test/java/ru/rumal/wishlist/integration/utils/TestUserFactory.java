package ru.rumal.wishlist.integration.utils;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import ru.rumal.wishlist.model.AuthType;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.service.UserService;

@Getter
@RequiredArgsConstructor
@TestConfiguration
public class TestUserFactory {

    private final UserService userService;
    private final String correctEmail = "test@test.com";
    private final String correctPasswordDecrypt = "testtest";
    private final String correctPassEncrypt = "$2a$12$2FhFNCHhQXC7A3iheMEITuxo9UkhwhTythl6aK2TN2SjBL8EYlIyC";
    private final String wrongEmail = "wrong@wrong.com";
    private final String wrongPassword = "wrong@wrong.com";

    public User getCorrectUser() {
        User user = new User();
        user.setEmail(correctEmail);
        user.setPassword(correctPassEncrypt);
        user.setPicture("some picture link");
        user.setEnable(true);
        user.setName("Testing test");
        user.setAuthType(AuthType.APPLICATION);
        String randomId = userService.generateRandomId(user);
        user.setId(randomId);
        return user;
    }

    public User getNewUser() {
        User user = new User();
        user.setEmail("new@new.com");
        user.setPassword("12345678");
        user.setPicture(null);
        user.setEnable(true);
        user.setName("New user");
        return user;
    }

    @Data
    public static class UserRegistrationRequest {
        private String email;
        private String password;
        private String name;

        public User toUser() {
            User user = new User();
            user.setEmail(this.email);
            user.setPassword(this.password);
            user.setName(this.name);
            user.setPicture("");
            user.setEnable(true);
            return user;
        }
    }
}
