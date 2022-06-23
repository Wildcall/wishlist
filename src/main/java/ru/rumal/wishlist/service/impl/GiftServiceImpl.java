package ru.rumal.wishlist.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rumal.wishlist.model.entity.Gift;
import ru.rumal.wishlist.repository.GiftRepo;
import ru.rumal.wishlist.service.CustomBeanUtils;
import ru.rumal.wishlist.service.GiftService;

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
    public List<Gift> getAllByUserId(String userId) {
        return giftRepo.getAllByUserId(userId);
    }

    @Override
    public boolean deleteByIdAndUserId(Long id,
                                       String userId) {
        log.info("Try to delete gift! id: {} / userId: {}", id, userId);
        Optional<Gift> gift = findByIdAndUserId(id, userId);
        gift.ifPresent(giftRepo::delete);
        return gift.isPresent();
    }

    @Override
    public Optional<Gift> updateByIdAndUserId(Gift gift) {
        Optional<Gift> optGift =
                findByIdAndUserId(gift.getId(), gift.getUser().getId());
        if (!optGift.isPresent())
            return Optional.empty();
        Gift existedGift = optGift.get();
        CustomBeanUtils.copyProperties(gift, existedGift, "id", "user", "giversSet", "eventsSet");
        return Optional.of(giftRepo.save(existedGift));
    }

    @Override
    public int getCountByUserId(String userId) {
        return giftRepo.countByUserId(userId);
    }

    public Optional<Gift> findByIdAndUserId(Long id,
                                            String userId) {
        return giftRepo.findByIdAndUserId(id, userId);
    }
}
