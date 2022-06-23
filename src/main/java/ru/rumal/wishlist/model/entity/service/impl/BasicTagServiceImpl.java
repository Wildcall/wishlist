package ru.rumal.wishlist.model.entity.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rumal.wishlist.model.entity.BasicTag;
import ru.rumal.wishlist.model.entity.service.BasicTagService;
import ru.rumal.wishlist.repository.BasicTagRepo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class BasicTagServiceImpl implements BasicTagService {
    private final BasicTagRepo basicTagRepo;

    @Override
    public BasicTag save(BasicTag basicEvent) {
        return basicTagRepo.save(basicEvent);
    }

    @Override
    public List<BasicTag> getAll() {
        return StreamSupport
                .stream(basicTagRepo.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(Long id) {
        Optional<BasicTag> event = basicTagRepo.findById(id);
        event.ifPresent(basicTagRepo::delete);
        return event.isPresent();
    }
}
