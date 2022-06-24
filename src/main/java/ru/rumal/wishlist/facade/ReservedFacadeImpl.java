package ru.rumal.wishlist.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rumal.wishlist.exception.BadRequestException;
import ru.rumal.wishlist.model.GiftStatus;
import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.entity.Event;
import ru.rumal.wishlist.model.entity.Gift;
import ru.rumal.wishlist.service.EventService;
import ru.rumal.wishlist.service.GiftService;
import ru.rumal.wishlist.service.JwtUtils;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservedFacadeImpl implements ReservedFacade {

    private final GiftService giftService;
    private final EventService eventService;
    private final JwtUtils jwtUtils;

    @Override
    public String generateLink(Principal principal,
                               Long eventId) {
        String userId = principal.getName();
        Event event = eventService
                .findByIdAndUserId(eventId, userId)
                .orElseThrow(() -> new BadRequestException("Event not found"));
        return jwtUtils.generateReservedToken(userId, event);
    }

    @Override
    public Set<BaseDto> getGifts(String token) {
        Event existedEvent = getExistedEvent(token);
        return existedEvent
                .getGifts()
                .stream()
                .map(Gift::toBaseDto)
                .collect(Collectors.toSet());
    }

    @Override
    public BaseDto getEvent(String token) {
        return getExistedEvent(token).toBaseDto();
    }

    @Override
    public List<Long> reserveGift(Principal principal,
                                  List<Long> giftsId,
                                  String token) {
        String userId = principal.getName();
        Event event = getExistedEvent(token);
        boolean var1 = event
                .getGifts()
                .stream()
                .anyMatch(g -> g.getStatus() != GiftStatus.NEW);
        if (var1)
            throw new BadRequestException("One of gifts already reserved or received!");

        for (Long giftId : giftsId) {
            if (!event
                    .getGifts()
                    .contains(new Gift(giftId)))
                throw new BadRequestException("Gift with id '" + giftId + "' not found!");
        }
        giftService.reserveList(userId, giftsId);
        return giftsId;
    }

    private Event getExistedEvent(String token) {
        Event event = jwtUtils
                .verifyReservedToken(token)
                .orElseThrow(() -> new BadRequestException("Reserved token not valid"));
        return eventService
                .findByIdAndUserId(event.getId(), event
                        .getUser()
                        .getId())
                .orElseThrow(() -> new BadRequestException("Event not found"));
    }
}
