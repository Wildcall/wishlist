package ru.rumal.wishlist.integration.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.repository.CrudRepository;

import java.util.Arrays;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Getter
@RequiredArgsConstructor
@TestConfiguration
public class DataBaseInitializer {

    @SafeVarargs
    public final <T> Stream<T> initDB(CrudRepository repo,
                                      T... t) {
        return StreamSupport.stream(repo
                                            .saveAll(Arrays.asList(t))
                                            .spliterator(), false);
    }

    public <T> void clearDB(CrudRepository repo) {
        repo.deleteAll();
    }
}
