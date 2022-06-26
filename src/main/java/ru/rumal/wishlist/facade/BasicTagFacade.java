package ru.rumal.wishlist.facade;

import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.BasicEventDto;
import ru.rumal.wishlist.model.dto.BasicTagDto;

import java.util.List;

public interface BasicTagFacade {
    BaseDto create(BasicTagDto basicTagDto);

    List<BaseDto> getAll();

    Long delete(Long id);
}
