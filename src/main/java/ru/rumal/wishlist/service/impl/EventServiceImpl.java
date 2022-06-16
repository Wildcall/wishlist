package ru.rumal.wishlist.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.entity.Event;
import ru.rumal.wishlist.service.EventService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {
    @Override
    public Event save(Event event) {
        return null;
    }

    @Override
    public List<Event> getAll(String email) {
        return null;
    }

    @Override
    public Long deleteByIdAndUserEmail(Long id,
                                       String email) {
        return null;
    }

    @Override
    public BaseDto updateByIdAndUserEmail(Long id,
                                          String email,
                                          Event event) {
        return null;
    }
}
