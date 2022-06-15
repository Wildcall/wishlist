package ru.rumal.wishlist.exception.facade;

import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.EventDto;

import java.util.List;

public interface EventFacade {
    BaseDto create(EventDto eventDto);

    List<BaseDto> getAll();

    Long delete(Long id);

    BaseDto update(Long id,
                   EventDto eventDto);
}
