package ru.rumal.wishlist.repository;

import org.springframework.data.repository.CrudRepository;
import ru.rumal.wishlist.model.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepo extends CrudRepository<Tag, Long> {

    Optional<Tag> findByIdAndUserId(Long id,
                                    String userId);

    List<Tag> findAllByUserId(String userId);

    int countByUserId(String userId);
}
