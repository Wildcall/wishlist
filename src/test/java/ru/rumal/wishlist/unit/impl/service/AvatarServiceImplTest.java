package ru.rumal.wishlist.unit.impl.service;

import org.junit.jupiter.api.Test;
import ru.rumal.wishlist.integration.utils.TestUserFactory;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.service.AvatarService;
import ru.rumal.wishlist.service.UserExtractor;
import ru.rumal.wishlist.service.impl.AvatarServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

class AvatarServiceImplTest {

    @Test
    void generate() {
        AvatarService avatarservice = new AvatarServiceImpl();
        TestUserFactory factory = new TestUserFactory();
        User vova = factory.getNewUser();
        vova.setName("Вова Семенов");
        System.out.println(avatarservice.generate(vova));
//        vova.setName("A");
//         my = "data:image/png;base64," + service.generate(vova);
//        System.out.println(my);
//        vova.setName("Vasya");
//         my = "data:image/png;base64," + service.generate(vova);
//        System.out.println(my);
//        vova.setName("Sereja");
//         my = "data:image/png;base64," + service.generate(vova);
//        System.out.println(my);
//        vova.setName("Vova");
//         my = "data:image/png;base64," + service.generate(vova);
//        System.out.println(my);
    }
}

