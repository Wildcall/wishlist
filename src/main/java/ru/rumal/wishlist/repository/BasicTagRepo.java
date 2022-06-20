package ru.rumal.wishlist.repository;

import org.springframework.data.repository.CrudRepository;
import ru.rumal.wishlist.model.entity.BasicTag;

public interface BasicTagRepo extends CrudRepository<BasicTag, Long> {
}
