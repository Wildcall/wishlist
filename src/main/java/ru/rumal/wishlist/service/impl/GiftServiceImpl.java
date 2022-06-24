package ru.rumal.wishlist.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rumal.wishlist.model.GiftStatus;
import ru.rumal.wishlist.model.entity.Gift;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.repository.GiftRepo;
import ru.rumal.wishlist.service.GiftService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
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
        Optional<Gift> gift = findByIdAndUserId(id, userId);
        gift.ifPresent(giftRepo::delete);
        return gift.isPresent();
    }

    @Override
    public int getCountByUserId(String userId) {
        return giftRepo.countByUserId(userId);
    }

    @Override
    public Optional<Gift> findByIdAndUserId(Long id,
                                            String userId) {
        return giftRepo.findByIdAndUserId(id, userId);
    }

    @Override
    public void reserveList(String userId,
                            List<Long> giftsId) {
        for (Long id : giftsId) {
            giftRepo
                    .findById(id)
                    .ifPresent(value -> {
                        value
                                .getGiversSet()
                                .add(new User(userId));
                        value.setStatus(GiftStatus.RESERVED);
                        giftRepo.save(value);
                    });
        }
    }
}
