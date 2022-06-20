package ru.rumal.wishlist.repository;

import org.springframework.data.repository.CrudRepository;
import ru.rumal.wishlist.model.entity.BasicEvent;

import java.util.Optional;

public interface BasicEventRepo extends CrudRepository<BasicEvent, Long> {
    Optional<BasicEvent> findById(Long id);

}
