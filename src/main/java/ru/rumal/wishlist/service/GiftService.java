package ru.rumal.wishlist.service;

import ru.rumal.wishlist.model.entity.Gift;

import java.util.List;
import java.util.Optional;

public interface GiftService {
    Gift save(Gift gift);

    List<Gift> getAllByUserId(String userId);

    boolean deleteByIdAndUserId(Long id,
                                String userId);

    int getCountByUserId(String userId);

    Optional<Gift> findByIdAndUserId(Long giftId,
                                     String userId);
}
