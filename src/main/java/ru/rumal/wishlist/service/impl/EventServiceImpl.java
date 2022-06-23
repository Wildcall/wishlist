package ru.rumal.wishlist.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rumal.wishlist.model.entity.BasicEvent;
import ru.rumal.wishlist.model.entity.Event;
import ru.rumal.wishlist.repository.BasicEventRepo;
import ru.rumal.wishlist.repository.EventRepo;
import ru.rumal.wishlist.service.CustomBeanUtils;
import ru.rumal.wishlist.service.EventService;
import ru.rumal.wishlist.repository.BasicEventRepo;
import ru.rumal.wishlist.repository.EventRepo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepo eventRepo;
    private final BasicEventRepo basicEventRepo;

    @Override
    public Event save(Event event) {
        return eventRepo.save(event);
    }

    @Override
    public List<Event> getAll(String id) {
        return eventRepo.findAllByUserId(id);
    }

    @Override
    public boolean deleteByIdAndUserId(Long id,
                                       String userId) {
        Optional<Event> event = findByIdAndUserId(id, userId);
        event.ifPresent(eventRepo::delete);
        return event.isPresent();
    }

    @Override
    public Optional<Event> updateByIdAndUserId(Event event) {
        Optional<Event> optEvent = findByIdAndUserId(event.getId(), event
                .getUser()
                .getId());
        if (!optEvent.isPresent()) return Optional.empty();
        Event existedEvent = optEvent.get();
        CustomBeanUtils.copyProperties(event, existedEvent, "id", "user");
        return Optional.of(eventRepo.save(existedEvent));
    }

    @Override
    public List<BasicEvent> getBasic() {
        return StreamSupport
                .stream(basicEventRepo
                                .findAll()
                                .spliterator(), false)
                .collect(Collectors.toList());
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
