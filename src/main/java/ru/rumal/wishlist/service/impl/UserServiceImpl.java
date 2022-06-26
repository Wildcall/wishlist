package ru.rumal.wishlist.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.rumal.wishlist.model.AuthType;
import ru.rumal.wishlist.model.Role;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.repository.UserRepo;
import ru.rumal.wishlist.service.AvatarService;
import ru.rumal.wishlist.service.UserService;

import java.util.Optional;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final Random random = new Random();
    private final UserRepo userRepo;
    private final AvatarService avatarService;

    @Override
    public Optional<User> save(User user) {
        if (existByEmail(user.getEmail())) return Optional.empty();

        if (user.getId() == null) user.setId(generateRandomId(user));

        if (user.getRole() == null) user.setRole(Role.USER);

        if (user.getAuthType() == null) user.setAuthType(AuthType.APPLICATION);

        if (user.getPicture() == null) user.setPicture(avatarService.generate(user));

        return Optional.of(userRepo.save(user));
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public boolean existByEmail(String email) {
        return userRepo
                .findByEmail(email)
                .isPresent();
    }

    @Override
    public boolean delete(String id) {
        Optional<User> user = userRepo.findById(id);
        user.ifPresent(userRepo::delete);
        return user.isPresent();
    }

    @Override
    public String generateRandomId(User user) {
        long value = 1000000L + Math.abs(random.nextLong());
        return String.valueOf(value);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepo
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));
    }
}
