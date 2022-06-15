package ru.rumal.wishlist.exception.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.GiftDto;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GiftFacadeImpl implements GiftFacade {
    @Override
    public BaseDto create(GiftDto giftDto) {
        return null;
    }

    @Override
    public List<BaseDto> getAll() {
        return null;
    }

    @Override
    public BaseDto update(Long id,
                          GiftDto giftDto) {
        return null;
    }

    @Override
    public Long delete(Long id) {
        return null;
    }
}
