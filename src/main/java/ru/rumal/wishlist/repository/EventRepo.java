package ru.rumal.wishlist.repository;

import org.springframework.data.repository.CrudRepository;
import ru.rumal.wishlist.model.entity.Event;

public interface EventRepo extends CrudRepository<Event, Long> {
    Long deleteByIdAndUserEmail(Long id,
                                String email);
}
