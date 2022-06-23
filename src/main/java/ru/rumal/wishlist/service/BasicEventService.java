package ru.rumal.wishlist.service;

import ru.rumal.wishlist.model.entity.BasicEvent;

import java.util.List;

public interface BasicEventService {
    BasicEvent save(BasicEvent basicEvent);
    List<BasicEvent> getAll();
    boolean deleteById(Long id);
}
