package ru.rumal.wishlist.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.repository.UserRepo;
import ru.rumal.wishlist.service.UserService;

import java.util.Optional;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    @Override
    public Optional<User> save(User user) {
        if (existByEmail(user.getEmail())) return Optional.empty();
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
        long leftLimit = 1000000L;
        long rightLimit = Long.MAX_VALUE;
        long generatedLong = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
        return String.valueOf(generatedLong);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return userRepo
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));
    }
}
