package ru.rumal.wishlist.repository;

import org.springframework.data.repository.CrudRepository;
import ru.rumal.wishlist.model.entity.Gift;

public interface GiftRepo extends CrudRepository<Gift, Long> {
}
