package ru.rumal.wishlist.model.entity.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rumal.wishlist.model.entity.BasicEvent;
import ru.rumal.wishlist.model.entity.service.BasicEventService;
import ru.rumal.wishlist.repository.BasicEventRepo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class BasicEventServiceImpl implements BasicEventService {

    private final BasicEventRepo basicEventRepo;

    @Override
    public BasicEvent save(BasicEvent basicEvent) {
        return basicEventRepo.save(basicEvent);
    }

    @Override
    public List<BasicEvent> getAll() {
        return StreamSupport
                .stream(basicEventRepo.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(Long id) {
        Optional<BasicEvent> event = basicEventRepo.findById(id);
        event.ifPresent(basicEventRepo::delete);
        return event.isPresent();
    }

}
