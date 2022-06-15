package ru.rumal.wishlist.service;

import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.entity.Event;

import java.util.List;

public interface EventService {
    Event save(Event event);

    List<Event> getAll(String email);

    Long deleteByIdAndUserEmail(Long id,
                                String email);

    BaseDto updateByIdAndUserEmail(Long id,
                                   String email,
                                   Event event);
}
