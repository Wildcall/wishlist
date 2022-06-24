package ru.rumal.wishlist.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rumal.wishlist.exception.BadRequestException;
import ru.rumal.wishlist.model.GiftStatus;
import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.GiftDto;
import ru.rumal.wishlist.model.entity.Event;
import ru.rumal.wishlist.model.entity.Gift;
import ru.rumal.wishlist.model.entity.Tag;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.service.CustomBeanUtils;
import ru.rumal.wishlist.service.EventService;
import ru.rumal.wishlist.service.GiftService;
import ru.rumal.wishlist.service.TagService;

import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class GiftFacadeImpl implements GiftFacade {

    private final GiftService giftService;
    private final TagService tagService;
    private final EventService eventService;
    @Value("${limits.gift:25}")
    @Min(1)
    private int giftLimit;

    @Override
    public List<BaseDto> getAllByUser(Principal principal) {
        String userId = principal.getName();
        List<Gift> events = giftService.getAllByUserId(userId);

        return events
                .stream()
                .map(Gift::toBaseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public BaseDto create(Principal principal,
                          GiftDto giftDto) {
        String userId = principal.getName();

        if (giftService.getCountByUserId(userId) >= giftLimit) throw new BadRequestException(
                "You can't create more then " + giftLimit + " gifts");

        Gift gift = (Gift) giftDto.toBaseEntity();
        gift.setUser(new User(userId));
        gift.setStatus(GiftStatus.NEW);
        gift.setTag(checkTagAvailable(giftDto.getTagId(), userId));
        Gift savedGift = giftService.save(gift);

        Long eventId = giftDto.getEventId();
        if (eventId != null) {
            Event event = eventService
                    .findByIdAndUserId(eventId, userId)
                    .orElseThrow(() -> new BadRequestException("Event with id '" + eventId + "' not found!"));
            event.addGift(savedGift);
        }

        return savedGift.toBaseDto();
    }

    @Transactional
    @Override
    public BaseDto update(Principal principal,
                          Long giftId,
                          GiftDto giftDto) {
        String userId = principal.getName();
        Gift existedGift = giftService
                .findByIdAndUserId(giftId, userId)
                .orElseThrow(() -> new BadRequestException("Gift with id '" + giftId + "' not found"));

        Gift gift = (Gift) giftDto.toBaseEntity();
        gift.setTag(checkTagAvailable(giftDto.getTagId(), userId));

        CustomBeanUtils.copyProperties(gift, existedGift, "id", "user", "giversSet", "eventsSet");

        return giftService
                .save(existedGift)
                .toBaseDto();
    }

    @Override
    public Long delete(Principal principal,
                       Long giftId) {
        String userId = principal.getName();

        if (giftService.deleteByIdAndUserId(giftId, userId)) return giftId;
        throw new BadRequestException("Gift with id '" + giftId + "' not found");
    }

    private Tag checkTagAvailable(Long tagId,
                                  String userId) {
        if (tagId != null) {
            return tagService
                    .findByIdAndUserId(tagId, userId)
                    .orElseThrow(() -> new BadRequestException("Tag with id: '" + tagId + "' not found"));
        }
        return null;
    }
}
