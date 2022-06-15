package ru.rumal.wishlist.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.EventDto;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventFacadeImpl implements EventFacade {
    @Override
    public BaseDto create(EventDto eventDto) {
        return null;
    }

    @Override
    public List<BaseDto> getAll() {
        return null;
    }

    @Override
    public Long delete(Long id) {
        return null;
    }

    @Override
    public BaseDto update(Long id,
                          EventDto eventDto) {
        return null;
    }
}
