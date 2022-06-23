package ru.rumal.wishlist.model.entity.service;

import ru.rumal.wishlist.model.entity.User;

public interface AvatarService {
    String generate(User user);

    void setWidth(int width);

    void setGrid(int grid);
}
