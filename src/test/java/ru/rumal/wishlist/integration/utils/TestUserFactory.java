package ru.rumal.wishlist.integration.utils;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import ru.rumal.wishlist.model.AuthType;
import ru.rumal.wishlist.model.Role;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.repository.UserRepo;
import ru.rumal.wishlist.service.UserService;

@Getter
@RequiredArgsConstructor
@TestConfiguration
public class TestUserFactory {

    private final UserService userService;
    private final UserRepo userRepo;
    private final String correctEmail = "test@test.com";
    private final String correctPasswordDecrypt = "testtest";
    private final String correctPassEncrypt = "$2a$12$2FhFNCHhQXC7A3iheMEITuxo9UkhwhTythl6aK2TN2SjBL8EYlIyC";
    private final String wrongEmail = "wrong@wrong.com";
    private final String wrongPassword = "wrong@wrong.com";
    private User correctUser = null;
    private User existedUser = null;

    public User getCorrectUser() {
        if (this.correctUser == null) {
            User user = new User();
            user.setEmail(correctEmail);
            user.setPassword(correctPassEncrypt);
            user.setName("Testing test");
            user.setPicture("some picture link");
            user.setAuthType(AuthType.APPLICATION);
            user.setEnable(true);
            user.setRole(Role.USER);
            String randomId = userService.generateRandomId(user);
            user.setId(randomId);
            this.correctUser = user;
            return user;
        }
        return this.correctUser;
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

    public void initDB() {
        userRepo.save(getCorrectUser());
    }

    public void clearDB() {
        userRepo.deleteAll();
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

    @Data
    public static class UserUpdateRequest {
        private String id;
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
