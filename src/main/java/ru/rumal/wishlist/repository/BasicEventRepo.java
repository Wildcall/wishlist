package ru.rumal.wishlist.repository;

import org.springframework.data.repository.CrudRepository;
import ru.rumal.wishlist.model.entity.BasicEvent;

public interface BasicEventRepo extends CrudRepository<BasicEvent, Long> {
}
