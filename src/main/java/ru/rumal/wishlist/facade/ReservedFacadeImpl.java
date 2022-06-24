package ru.rumal.wishlist.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rumal.wishlist.exception.BadRequestException;
import ru.rumal.wishlist.model.GiftStatus;
import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.entity.Event;
import ru.rumal.wishlist.model.entity.Gift;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.service.EventService;
import ru.rumal.wishlist.service.JwtUtils;
import ru.rumal.wishlist.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservedFacadeImpl implements ReservedFacade {

    private final UserService userService;
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
    public BaseDto getEvent(String token) {
        return getExistedEvent(token).toBaseDto();
    }

    @Transactional
    @Override
    public List<BaseDto> reserveGift(Principal principal,
                                     List<Long> giftsId,
                                     String token) {
        String userId = principal.getName();
        User user = userService
                .findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found!"));
        if (giftsId != null) {
            Event event = getExistedEvent(token);
            Set<Gift> gifts = event.getGifts();

            //  @formatter:off
            for (Long giftId : giftsId) {
                Gift gift = gifts
                        .stream()
                        .filter(g -> g.getId().equals(giftId))
                        .findFirst()
                        .orElseThrow(() -> new BadRequestException("Gift with id '" + giftId + "' not found!"));
                if (!gift.getStatus().equals(GiftStatus.NEW))
                    throw new BadRequestException("Gift with id '" + gift.getId() +"' already reserved or received!");
                gift.setStatus(GiftStatus.RESERVED);
                user.getGivingGiftsSet().add(gift);
            }
            //  @formatter:on
            userService.save(user);
        }
        return user
                .getGivingGiftsSet()
                .stream()
                .map(Gift::toBaseDto)
                .collect(Collectors.toList());
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
