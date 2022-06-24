package ru.rumal.wishlist.facade;

import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.EventDto;

import java.security.Principal;
import java.util.List;

public interface EventFacade {
    BaseDto create(Principal principal,
                   EventDto eventDto);

    List<BaseDto> getAllByUser(Principal principal);


    Long delete(Principal principal,
                Long id);

    BaseDto update(Principal principal,
                   Long id,
                   EventDto eventDto);
}
