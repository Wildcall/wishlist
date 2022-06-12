package ru.rumal.wishlist.service;

import ru.rumal.wishlist.model.User;

public interface UserService {
    void save(User user);

    boolean existByEmail(String email);
}
