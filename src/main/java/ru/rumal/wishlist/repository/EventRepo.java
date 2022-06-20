package ru.rumal.wishlist.repository;

import org.springframework.data.repository.CrudRepository;
import ru.rumal.wishlist.model.entity.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepo extends CrudRepository<Event, Long> {
    List<Event> findAllByUserId(String id);

    Optional<Event> findByIdAndUserId(Long id,
                                      String userId);
}
