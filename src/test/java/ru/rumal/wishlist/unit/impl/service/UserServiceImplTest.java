package ru.rumal.wishlist.unit.impl.service;

import liquibase.repackaged.org.apache.commons.lang3.math.NumberUtils;
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
        for (int i = 0; i < 100; i++) {
            String buffer = impl.generateRandomId(null);
            assertNotEquals(null, buffer);
            assertFalse(buffer.startsWith("-"));
            assertTrue(NumberUtils.isDigits(buffer));
            assertFalse(set.contains(buffer));
            set.add(buffer);
        }
    }
}