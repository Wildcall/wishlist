package ru.rumal.wishlist.integration.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import ru.rumal.wishlist.model.entity.Event;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.repository.EventRepo;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Getter
@RequiredArgsConstructor
@TestConfiguration
public class TestEventFactory {

    //  @formatter:off
    private final Random random = new Random();
    @Autowired private EventRepo eventRepo;
    //  @formatter:on

    public List<Event> generateRandomEvent(int count,
                                           User user) {
        List<Event> events = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            events.add(generateRandomEvent(user));
        }
        return events;
    }

    public Event generateRandomEvent(User user) {
        Event event = new Event();
        event.setName(String.valueOf(random.nextInt(1000)));
        event.setDate(LocalDateTime.now().minusMinutes(random.nextInt(1000)));
        event.setDescription(String.valueOf(random.nextInt(1000)));
        event.setUser(user);
        return event;
    }

    public Event save(Event event) {
        return eventRepo.save(event);
    }

    public List<Event> saveAll(List<Event> events) {
        return StreamSupport
                .stream(eventRepo
                                .saveAll(events)
                                .spliterator(), false)
                .collect(Collectors.toList());
    }

    public Event findById(Long id) {
        return eventRepo
                .findById(id)
                .orElse(null);
    }

    public void clear() {
        eventRepo.deleteAll();
    }
}
