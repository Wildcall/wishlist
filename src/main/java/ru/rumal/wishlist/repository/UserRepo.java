package ru.rumal.wishlist.repository;

import org.springframework.data.repository.CrudRepository;
import ru.rumal.wishlist.model.entity.User;

import java.util.Optional;

public interface UserRepo extends CrudRepository<User, String> {

    Optional<User> findByEmail(String email);
}
