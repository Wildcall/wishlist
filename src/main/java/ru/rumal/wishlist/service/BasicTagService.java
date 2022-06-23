package ru.rumal.wishlist.service;

import ru.rumal.wishlist.model.entity.BasicTag;

import java.util.List;

public interface BasicTagService {
    BasicTag save(BasicTag basicEvent);
    List<BasicTag> getAll();
    boolean deleteById(Long id);
}
