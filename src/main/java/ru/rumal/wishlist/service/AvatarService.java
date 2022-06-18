package ru.rumal.wishlist.service;

import ru.rumal.wishlist.model.entity.User;

public interface AvatarService {
    String generate(User user);
    void setWidth(int wight);
    void setGrid(int grid);
}
