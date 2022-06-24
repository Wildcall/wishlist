package ru.rumal.wishlist.facade;

import ru.rumal.wishlist.model.dto.BaseDto;

import java.security.Principal;
import java.util.List;
import java.util.Set;

public interface ReservedFacade {
    String generateLink(Principal principal,
                        Long eventId);

    List<BaseDto> reserveGift(Principal principal,
                           List<Long> giftsId,
                           String token);

    BaseDto getEvent(String token);
}
