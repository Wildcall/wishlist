package ru.rumal.wishlist.model.dto;

import ru.rumal.wishlist.model.entity.BaseEntity;

public interface BaseDto {
    BaseEntity toBaseEntity();
}
