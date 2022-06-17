package ru.rumal.wishlist.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.service.AvatarService;

@Slf4j
@RequiredArgsConstructor
@Service
public class AvatarServiceImpl implements AvatarService {

    @Override
    public String generate(User user) {
        return "";
    }
}
