package ru.rumal.wishlist.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rumal.wishlist.exception.BadRequestException;
import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.BasicEventDto;
import ru.rumal.wishlist.model.entity.BasicEvent;
import ru.rumal.wishlist.model.entity.service.BasicEventService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasisEventFacadeImpl implements BasicEventFacade {

    private final BasicEventService basicEventService;


    @Override
    public BaseDto create(BasicEventDto basicEventDto) {
        BasicEvent basicEvent = (BasicEvent) basicEventDto.toBaseEntity();
        return basicEventService
                .save(basicEvent)
                .toBaseDto();
    }

    @Override
    public List<BaseDto> getAll() {
        List<BasicEvent> events = basicEventService.getAll();
        return events
                .stream()
                .map(BasicEvent::toBaseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Long delete(Long id) {
        if (basicEventService.deleteById(id)) return id;
        throw new BadRequestException("Basic event not found");
    }

}
