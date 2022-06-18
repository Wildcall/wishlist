package ru.rumal.wishlist.unit.impl.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.rumal.wishlist.service.UserService;
import ru.rumal.wishlist.service.impl.UserServiceImpl;

import java.util.HashSet;

class UserServiceImplTest {

    @Test
    void generateRandomId_isNotEqual() {
        UserService impl = new UserServiceImpl(null);
        HashSet<String> set = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            String buffer = impl.generateRandomId(null);
            Assertions.assertNotEquals(null, buffer);
            Assertions.assertEquals(false, set.contains(buffer));
            set.add(buffer);
        }
    }

}