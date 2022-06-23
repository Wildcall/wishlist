package ru.rumal.wishlist.facade;

import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.GiftDto;

import java.security.Principal;
import java.util.List;

public interface GiftFacade {
    BaseDto create(Principal principal,
                   GiftDto giftDto);

    List<BaseDto> getAllByUser(Principal principal);

    BaseDto update(Principal principal,
                   Long id,
                   GiftDto giftDto);

    Long delete(Principal principal,
                Long id);
}
