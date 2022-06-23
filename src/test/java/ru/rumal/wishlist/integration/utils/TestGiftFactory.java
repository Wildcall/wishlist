package ru.rumal.wishlist.integration.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import ru.rumal.wishlist.model.GiftStatus;
import ru.rumal.wishlist.model.entity.Gift;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.repository.GiftRepo;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Getter
@RequiredArgsConstructor
@TestConfiguration
public class TestGiftFactory {

    //  @formatter:off
    private final Random random = new Random();
    @Autowired private GiftRepo giftRepo;
    //  @formatter:on

    public List<Gift> generateRandomGift(int count,
                                         User user) {
        List<Gift> gifts = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            gifts.add(generateRandomGift(user));
        }
        return gifts;
    }

    public Gift generateRandomGift(User user) {
        Gift gift = new Gift();
        gift.setName(String.valueOf(random.nextInt(1000)));
        gift.setStatus(GiftStatus.NEW);
        gift.setLink(String.valueOf(random.nextInt(1000)));
        gift.setDescription(String.valueOf(random.nextInt(1000)));
        gift.setPicture(String.valueOf(random.nextInt(1000)));
        gift.setUser(user);
        return gift;
    }

    public Gift save(Gift gift) {
        return giftRepo.save(gift);
    }

    public List<Gift> saveAll(List<Gift> gifts) {
        return StreamSupport
                .stream(giftRepo
                                .saveAll(gifts)
                                .spliterator(), false)
                .collect(Collectors.toList());
    }

    public Gift findById(Long id) {
        return giftRepo
                .findById(id)
                .orElse(null);
    }
}
