package ru.rumal.wishlist.repository;

import org.springframework.data.repository.CrudRepository;
import ru.rumal.wishlist.model.entity.BasicEvent;
import ru.rumal.wishlist.model.entity.BasicTag;

import java.util.Optional;

public interface BasicTagRepo extends CrudRepository<BasicTag, Long> {
    Optional<BasicTag> findById(Long id);

}
