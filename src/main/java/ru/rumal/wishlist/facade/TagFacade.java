package ru.rumal.wishlist.facade;

import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.TagDto;

import java.security.Principal;
import java.util.List;

public interface TagFacade {
    BaseDto create(Principal principal,
                   TagDto tagDto);

    List<BaseDto> getAll(Principal principal);

    Long delete(Principal principal,
                Long id);

    BaseDto update(Principal principal,
                   Long id,
                   TagDto tagDto);
}
