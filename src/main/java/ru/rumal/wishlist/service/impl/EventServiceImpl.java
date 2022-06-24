package ru.rumal.wishlist.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rumal.wishlist.model.entity.Event;
import ru.rumal.wishlist.repository.EventRepo;
import ru.rumal.wishlist.service.CustomBeanUtils;
import ru.rumal.wishlist.service.EventService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private final EventRepo eventRepo;

    @Override
    public Event save(Event event) {
        return eventRepo.save(event);
    }

    @Override
    public List<Event> findAllByUserId(String userId) {
        return eventRepo.findAllByUserId(userId);
    }

    @Override
    public boolean deleteByIdAndUserId(Long id,
                                       String userId) {
        Optional<Event> event = findByIdAndUserId(id, userId);
        event.ifPresent(eventRepo::delete);
        return event.isPresent();
    }

    @Override
    public int getCountByUserId(String userId) {
        return eventRepo.countByUserId(userId);
    }

    @Override
    public Optional<Event> findByIdAndUserId(Long id,
                                             String userId) {
        return eventRepo.findByIdAndUserId(id, userId);
    }
}
