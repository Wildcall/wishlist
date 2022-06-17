package ru.rumal.wishlist.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.rumal.wishlist.model.AuthType;
import ru.rumal.wishlist.repository.UserRepo;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    @Mock
    UserRepo userRepo;

    @DisplayName("Testing id generator with 21 random numbers")
    @Test
    void idGenerator() {
        UserServiceImpl userService = new UserServiceImpl(userRepo);
        String s = userService.idGenerator(AuthType.APPLICATION);
        Assertions.assertTrue(s.startsWith("application"));
        Assertions.assertTrue(s.substring(12).matches("\\d{21}"));
    }
}