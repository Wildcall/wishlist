package ru.rumal.wishlist.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.rumal.wishlist.model.entity.User;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    Optional<User> save(User user);

    Optional<User> update(User user);

    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);

    boolean existByEmail(String email);

    boolean existById(String id);

    boolean delete(String id);

    String generateRandomId(User user);
}
