package ru.rumal.wishlist.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rumal.wishlist.model.entity.BasicTag;
import ru.rumal.wishlist.model.entity.Tag;
import ru.rumal.wishlist.repository.BasicTagRepo;
import ru.rumal.wishlist.repository.TagRepo;
import ru.rumal.wishlist.service.TagService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepo tagRepo;
    private final BasicTagRepo basicTagRepo;

    @Override
    public Tag save(Tag tag) {
        return tagRepo.save(tag);
    }

    @Override
    public List<Tag> getAll(String userId) {
        return tagRepo.findAllByUserId(userId);
    }

    @Override
    public boolean deleteByIdAndUserId(Long id,
                                       String userId) {
        Optional<Tag> tag = findByIdAndUserId(id, userId);
        tag.ifPresent(tagRepo::delete);
        return tag.isPresent();
    }

    @Override
    public Optional<Tag> updateByIdAndUserId(Long id,
                                             String userId,
                                             Tag tag) {
        return Optional.empty();
    }

    public Optional<Tag> findByIdAndUserId(Long id,
                                           String userId) {
        return tagRepo.findByIdAndUserId(id, userId);
    }

    @Override
    public int getCountByUserId(String userId) {
        return tagRepo.countByUserId(userId);
    }
}
