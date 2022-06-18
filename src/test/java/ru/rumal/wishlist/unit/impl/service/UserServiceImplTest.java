package ru.rumal.wishlist.unit.impl.service;

import org.junit.jupiter.api.Test;
import ru.rumal.wishlist.service.UserService;
import ru.rumal.wishlist.service.impl.UserServiceImpl;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    @Test
    void generateRandomId() {
        UserService impl = new UserServiceImpl(null);
        HashSet<String> set = new HashSet<>();
        for (int i = 0; i < 500; i++) {
            String buffer = impl.generateRandomId(null);
            assertNotEquals(null, buffer);
            assertTrue(buffer.length() > 7);
            assertFalse(set.contains(buffer));
            set.add(buffer);
        }
    }

}