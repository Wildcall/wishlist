package ru.rumal.wishlist.facade;

import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.BasicEventDto;

import java.util.List;

public interface BasicEventFacade {
    BaseDto create(BasicEventDto basicEventDto);

    List<BaseDto> getAll();

    Long delete(Long id);

}
