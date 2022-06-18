package ru.rumal.wishlist.unit.impl.service;

import org.junit.jupiter.api.Test;
import ru.rumal.wishlist.service.AvatarService;
import ru.rumal.wishlist.service.impl.AvatarServiceImpl;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AvatarServiceImplTest {

    @Test
    void generate() {
        AvatarService impl = new AvatarServiceImpl();
        Set<String> set = new HashSet<>();
        for (int i = 0; i < 15; i++) {
            String buffer = impl.generate(null);
            assertNotEquals(null, buffer);
            assertTrue(buffer.length() > 100);
            assertFalse(set.contains(buffer));
            set.add(buffer);
        }
    }

}

