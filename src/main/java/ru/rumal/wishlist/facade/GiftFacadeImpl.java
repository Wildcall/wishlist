package ru.rumal.wishlist.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.rumal.wishlist.exception.BadRequestException;
import ru.rumal.wishlist.model.GiftStatus;
import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.GiftDto;
import ru.rumal.wishlist.model.entity.Event;
import ru.rumal.wishlist.model.entity.Gift;
import ru.rumal.wishlist.model.entity.Tag;
import ru.rumal.wishlist.model.entity.User;
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

    @Override
    public BaseDto create(Principal principal,
                          GiftDto giftDto) {
        String userId = principal.getName();

        if (giftService.getCountByUserId(userId) >= giftLimit)
            throw new BadRequestException("You can't create more then " + giftLimit + " gifts");

        Gift gift = (Gift) giftDto.toBaseEntity();
        gift.setUser(new User(userId));
        gift.setStatus(GiftStatus.NEW);

        checkEventAvailable(giftDto.getEventId(), userId, gift);
        checkTagAvailable(giftDto.getTagId(), userId, gift);

        return giftService
                .save(gift)
                .toBaseDto();
    }

    @Override
    public BaseDto update(Principal principal,
                          Long id,
                          GiftDto giftDto) {
        String userId = principal.getName();
        Gift gift = (Gift) giftDto.toBaseEntity();

        gift.setId(id);
        gift.setUser(new User(userId));

        checkTagAvailable(giftDto.getTagId(), userId, gift);

        return giftService
                .updateByIdAndUserId(gift)
                .orElseThrow(() -> new BadRequestException("Gift not found"))
                .toBaseDto();
    }

    @Override
    public Long delete(Principal principal, Long id) {
        String userId = principal.getName();

        if (giftService.deleteByIdAndUserId(id, userId)) return id;
        throw new BadRequestException("Gift not found");
    }

    private void checkTagAvailable(Long tagId,
                                   String userId,
                                   Gift gift) {
        if (tagId != null) {
            Tag tag = tagService
                    .findByIdAndUserId(tagId, userId)
                    .orElseThrow(() -> new BadRequestException("Tag with id: '" + tagId + "' not found"));
            gift.setTag(tag);
        }
    }

    private void checkEventAvailable(Long eventId,
                                     String userId,
                                     Gift gift) {
        if (eventId != null) {
            Event event = eventService
                    .findByIdAndUserId(eventId, userId)
                    .orElseThrow(() -> new BadRequestException("Event with id: '" + eventId + "' not found"));
            gift.addEvent(event);
        }
    }
}
