package ru.rumal.wishlist.unit.impl.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rumal.wishlist.repository.UserRepo;
import ru.rumal.wishlist.model.entity.service.AvatarService;
import ru.rumal.wishlist.model.entity.service.UserService;
import ru.rumal.wishlist.model.entity.service.impl.UserServiceImpl;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private AvatarService avatarService;

    private final UserService userService = new UserServiceImpl(userRepo, avatarService);

    @Test
    void generateRandomId() {
        String regex = "\\d+";
        HashSet<String> set = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            String buffer = userService.generateRandomId(null);
            assertNotEquals(null, buffer);
            assertTrue(buffer.matches(regex));
            assertFalse(set.contains(buffer));
            set.add(buffer);
        }
    }
}