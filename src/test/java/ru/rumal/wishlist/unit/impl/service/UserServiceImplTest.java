package ru.rumal.wishlist.unit.impl.service;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import ru.rumal.wishlist.service.UserService;
import ru.rumal.wishlist.service.impl.UserServiceImpl;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    @Test
    void generateRandomId() {
        String regex = "\\d+";
        UserService impl = new UserServiceImpl(null);
        HashSet<String> set = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            String buffer = impl.generateRandomId(null);
            assertNotEquals(null, buffer);
            assertTrue(buffer.matches(regex));
            assertFalse(set.contains(buffer));
            set.add(buffer);
        }
    }
}