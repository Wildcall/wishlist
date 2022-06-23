package ru.rumal.wishlist.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.rumal.wishlist.exception.BadRequestException;
import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.EventDto;
import ru.rumal.wishlist.model.entity.BasicEvent;
import ru.rumal.wishlist.model.entity.Event;
import ru.rumal.wishlist.model.entity.Gift;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.service.EventService;
import ru.rumal.wishlist.service.GiftService;

import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventFacadeImpl implements EventFacade {

    private final EventService eventService;
    private final GiftService giftService;
    @Value("${limits.event:20}")
    @Min(1)
    private int eventLimit;

    @Override
    public BaseDto create(Principal principal,
                          EventDto eventDto) {
        String userId = principal.getName();

        if (eventService.getCountByUserId(userId) >= eventLimit)
            throw new BadRequestException("You can't create more then " + eventLimit + " events");

        Event event = (Event) eventDto.toBaseEntity();
        event.setUser(new User(userId));

        return eventService
                .save(event)
                .toBaseDto();
    }

    @Override
    public List<BaseDto> getAllByUser(Principal principal) {
        String userId = principal.getName();
        List<Event> events = eventService.getAll(userId);
        return events
                .stream()
                .map(Event::toBaseDto)
                .collect(Collectors.toList());
    }

    @Override
    public BaseDto update(Principal principal,
                          Long id,
                          EventDto eventDto) {
        String userId = principal.getName();
        Event event = (Event) eventDto.toBaseEntity();
        event.setId(id);
        event.setUser(new User(userId));

        checkGiftAvailable(eventDto.getGiftsSet(), userId, event);

        return eventService
                .updateByIdAndUserId(event)
                .orElseThrow(() -> new BadRequestException("Event not found"))
                .toBaseDto();
    }

    @Override
    public Long delete(Principal principal,
                       Long id) {
        String userId = principal.getName();
        if (eventService.deleteByIdAndUserId(id, userId)) return id;
        throw new BadRequestException("Event not found");
    }

    @Override
    public List<BaseDto> getBasic() {
        List<BasicEvent> basic = eventService.getBasic();

        return basic
                .stream()
                .map(BasicEvent::toBaseDto)
                .collect(Collectors.toList());
    }

    private void checkGiftAvailable(Set<Long> gifts,
                                    String userId,
                                    Event event) {
        if (gifts != null) {
            gifts
                    .forEach(giftId -> {
                        Gift gift = giftService
                                .findByIdAndUserId(giftId, userId)
                                .orElseThrow(() -> new BadRequestException("Gift with id: '" + giftId + "' not found"));
                        event.addGift(gift);
                    });
        }
    }
}
