package ru.rumal.wishlist.service;

import ru.rumal.wishlist.model.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> save(User user);

    Optional<User> findByEmail(String email);

    boolean existByEmail(String email);
}
