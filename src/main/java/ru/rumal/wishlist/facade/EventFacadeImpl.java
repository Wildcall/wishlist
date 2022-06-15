package ru.rumal.wishlist.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.EventDto;
import ru.rumal.wishlist.model.entity.Event;
import ru.rumal.wishlist.service.EventService;
import ru.rumal.wishlist.service.UserExtractor;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventFacadeImpl implements EventFacade {

    private final EventService eventService;
    private final UserExtractor userExtractor;

    @Override
    public BaseDto create(EventDto eventDto) {
        Event event = (Event) eventDto.toBaseEntity();
        return eventService
                .save(event)
                .toBaseDto();
    }

    @Override
    public List<BaseDto> getAll() {
        String email = userExtractor.extractEmail();
        List<Event> events = eventService.getAll(email);
        return events
                .stream()
                .map(Event::toBaseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Long delete(Long id) {
        String email = userExtractor.extractEmail();
        return eventService.deleteByIdAndUserEmail(id, email);
    }

    @Override
    public BaseDto update(Long id,
                          EventDto eventDto) {
        String email = userExtractor.extractEmail();
        Event event = (Event) eventDto.toBaseEntity();
        return eventService.updateByIdAndUserEmail(id, email, event);
    }
}
