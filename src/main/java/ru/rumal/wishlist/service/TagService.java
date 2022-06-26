package ru.rumal.wishlist.service;

import ru.rumal.wishlist.model.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {
    Tag save(Tag tag);

    List<Tag> getAll(String userId);

    boolean deleteByIdAndUserId(Long id,
                                String userId);

    Optional<Tag> updateByIdAndUserId(Long id,
                                      String userId,
                                      Tag tag);

    Optional<Tag> findByIdAndUserId(Long tagId,
                                    String userId);

    int getCountByUserId(String userId);
}
