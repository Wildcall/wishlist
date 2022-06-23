package ru.rumal.wishlist.repository;

import org.springframework.data.repository.CrudRepository;
import ru.rumal.wishlist.model.entity.Gift;

import java.util.List;
import java.util.Optional;

public interface GiftRepo extends CrudRepository<Gift, Long> {

    List<Gift> getAllByUserId(String userId);

    Optional<Gift> findByIdAndUserId(Long id,
                                     String userId);

    int countByUserId(String userId);
}
