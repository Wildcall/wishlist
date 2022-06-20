package ru.rumal.wishlist.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rumal.wishlist.exception.BadRequestException;
import ru.rumal.wishlist.model.GiftStatus;
import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.GiftDto;
import ru.rumal.wishlist.model.entity.Event;
import ru.rumal.wishlist.model.entity.Gift;
import ru.rumal.wishlist.model.entity.Tag;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.service.GiftService;
import ru.rumal.wishlist.service.TagService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class GiftFacadeImpl implements GiftFacade {

    private final GiftService giftService;
    private final TagService tagService;

    @Override
    public BaseDto create(Principal principal,
                          GiftDto giftDto) {
        String userId = principal.getName();

        Gift gift = (Gift) giftDto.toBaseEntity();
        gift.setUser(new User(userId));
        gift.setStatus(GiftStatus.NEW);

        Long tagId = giftDto.getTagId();
        if (tagId != null) {
            Tag tag = tagService
                    .findByIdAndUserId(tagId, userId)
                    .orElseThrow(() -> new BadRequestException("Tag with id: '" + tagId + "' not found"));
            gift.setTag(tag);
        }

        return giftService
                .save(gift)
                .toBaseDto();
    }

    @Override
    public List<BaseDto> getAll(Principal principal) {
        String userId = principal.getName();
        List<Gift> events = giftService.getAll(userId);

        return events
                .stream()
                .map(Gift::toBaseDto)
                .collect(Collectors.toList());
    }

    @Override
    public BaseDto update(Principal principal,
                          Long id,
                          GiftDto giftDto) {
        String userId = principal.getName();

        Event event = (Event) giftDto.toBaseEntity();
        return giftService
                .updateByIdAndUserId(id, userId, event)
                .orElseThrow(() -> new BadRequestException("Gift not found"))
                .toBaseDto();
    }

    @Override
    public Long delete(Principal principal,
                       Long id) {
        String userId = principal.getName();

        if (giftService.deleteByIdAndUserId(id, userId)) return id;
        throw new BadRequestException("Gift not found");
    }
}
