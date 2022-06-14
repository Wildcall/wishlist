package ru.rumal.wishlist.model.entity;

import ru.rumal.wishlist.model.dto.BaseDto;

public interface BaseEntity {
    BaseDto toBaseDto();
}
