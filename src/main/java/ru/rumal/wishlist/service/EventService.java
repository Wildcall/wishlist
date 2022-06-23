package ru.rumal.wishlist.service;

import ru.rumal.wishlist.model.entity.BasicEvent;
import ru.rumal.wishlist.model.entity.Event;

import java.util.List;
import java.util.Optional;

public interface EventService {
    Event save(Event event);

    List<Event> getAll(String id);

    boolean deleteByIdAndUserId(Long id,
                                String userId);

    Optional<Event> updateByIdAndUserId(Long id,
                                        String userId,
                                        Event event);

    List<BasicEvent> getBasic();

    int getCountByUserId(String userId);

    Optional<Event> findByIdAndUserId(Long id,
                                      String userId);
}
