package ru.rumal.wishlist.model.entity.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rumal.wishlist.model.entity.Event;
import ru.rumal.wishlist.model.entity.Gift;
import ru.rumal.wishlist.repository.GiftRepo;
import ru.rumal.wishlist.model.entity.service.GiftService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class GiftServiceImpl implements GiftService {

    private final GiftRepo giftRepo;

    @Override
    public Gift save(Gift gift) {
        return giftRepo.save(gift);
    }

    @Override
    public List<Gift> getAll(String userId) {
        return giftRepo.findAllByUserId(userId);
    }

    @Override
    public boolean deleteByIdAndUserId(Long id,
                                       String userId) {
        Optional<Gift> gift = findByIdAndUserId(id, userId);
        gift.ifPresent(giftRepo::delete);
        return gift.isPresent();
    }

    @Override
    public Optional<Gift> updateByIdAndUserId(Long id,
                                              String userId,
                                              Event event) {
        return Optional.empty();
    }

    public Optional<Gift> findByIdAndUserId(Long id,
                                            String userId) {
        return giftRepo.findByIdAndUserId(id, userId);
    }
}
