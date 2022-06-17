package ru.rumal.wishlist.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.rumal.wishlist.model.AuthType;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.repository.UserRepo;
import ru.rumal.wishlist.service.AvatarService;
import ru.rumal.wishlist.service.UserService;

import java.util.Locale;
import java.util.Optional;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final Random random = new Random();
    private final AvatarService avatarService;

    @Override
    public Optional<User> save(User user) {
        if (existByEmail(user.getEmail()))
            return Optional.empty();
        user.setId(generateRandomId(user));
        if (user.getPicture() == null)
            user.setPicture(avatarService.generate(user));
        return Optional.of(userRepo.save(user));
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
    public boolean delete(User user) {
        userRepo.delete(user);
        return true;
    }

    @Override
    public String generateRandomId(User user) {
        AuthType authType = user.getAuthType();
        if (authType == null)
            authType = AuthType.APPLICATION;

        StringBuilder str = new StringBuilder();
        str
                .append(authType.name().toLowerCase(Locale.ROOT))
                .append("-");
        for (int i = 0; i < 21; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return userRepo
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));
    }
}
