package ru.rumal.wishlist.model.entity.service;

import ru.rumal.wishlist.model.entity.Event;
import ru.rumal.wishlist.model.entity.Gift;

import java.util.List;
import java.util.Optional;

public interface GiftService {
    Gift save(Gift gift);

    List<Gift> getAll(String userId);

    boolean deleteByIdAndUserId(Long id,
                                String userId);

    Optional<Gift> updateByIdAndUserId(Long id,
                                       String userId,
                                       Event event);
}
