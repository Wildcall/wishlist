package ru.rumal.wishlist.facade;

import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.GiftDto;

import java.util.List;

public interface GiftFacade {
    BaseDto create(GiftDto giftDto);

    List<BaseDto> getAll();

    BaseDto update(Long id,
                   GiftDto giftDto);

    Long delete(Long id);
}
