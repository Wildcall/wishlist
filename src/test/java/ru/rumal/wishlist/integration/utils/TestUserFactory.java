package ru.rumal.wishlist.integration.utils;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import ru.rumal.wishlist.model.AuthType;
import ru.rumal.wishlist.model.Role;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.repository.UserRepo;

@Getter
@RequiredArgsConstructor
@TestConfiguration
public class TestUserFactory {

    private final UserRepo userRepo;
    private final String passEncrypt = "$2a$12$Q8gugRiiCMOtXKVpMraVquQOrAHvPOZ3qw/QZAEXfRgi8awxUYcxO"; // 12345678
    private final String passDecrypt = "12345678";
    private User correctUser = null;
    private User newUser = null;
    private User existedUser = null;
    private User tmpUser = null;

    public User getCorrectUser() {
        if (this.correctUser == null) {
            User user = new User();
            user.setId("1");
            user.setEmail("correct@user.com");
            user.setPassword(passEncrypt);
            user.setName("Correct User");
            user.setAuthType(AuthType.APPLICATION);
            user.setEnable(true);
            user.setRole(Role.USER);
            this.correctUser = user;
            return user;
        }
        return this.correctUser;
    }

    public User getExistedUser() {
        if (this.existedUser == null) {
            User user = new User();
            user.setId("2");
            user.setEmail("existed@user.com");
            user.setPassword(passEncrypt);
            user.setName("Existed User");
            user.setAuthType(AuthType.APPLICATION);
            user.setEnable(true);
            user.setRole(Role.USER);
            this.existedUser = user;
            return user;
        }
        return this.existedUser;
    }

    public User getNewUser() {
        if (this.newUser == null) {
            User user = new User();
            user.setEmail("new@user.com");
            user.setPassword(passEncrypt);
            user.setName("New User");
            user.setAuthType(AuthType.APPLICATION);
            user.setEnable(true);
            user.setRole(Role.USER);
            this.newUser = user;
            return user;
        }
        return this.newUser;
    }

    public User getTmpUser() {
        if (this.tmpUser == null) {
            User user = new User();
            user.setId("temporal");
            user.setEmail("temporal@user.com");
            user.setPassword(passEncrypt);
            user.setName("Temporal User");
            user.setAuthType(AuthType.APPLICATION);
            user.setEnable(true);
            user.setRole(Role.USER);
            this.tmpUser = user;
            return user;
        }
        return this.tmpUser;
    }

    public User findById(String id) {
        return userRepo
                .findById(id)
                .orElse(null);
    }

    public User save(User user) {
        return userRepo.save(user);
    }

    public void clear(String id) {
        userRepo.deleteById(id);
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
    public static class UserUpdateInfoRequest {
        private String email;
        private String name;
    }

    @Data
    public static class UserUpdatePasswordRequest {
        private String password;
        private String newPassword;
    }
}
