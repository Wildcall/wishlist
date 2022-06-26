package ru.rumal.wishlist.service;

import ru.rumal.wishlist.model.entity.Event;

import java.util.List;
import java.util.Optional;

public interface EventService {

    Event save(Event event);

    List<Event> findAllByUserId(String userId);

    boolean deleteByIdAndUserId(Long id,
                                String userId);

    int getCountByUserId(String userId);

    Optional<Event> findByIdAndUserId(Long id,
                                      String userId);
}
