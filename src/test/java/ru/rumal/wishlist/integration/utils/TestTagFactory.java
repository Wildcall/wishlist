package ru.rumal.wishlist.integration.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import ru.rumal.wishlist.model.entity.Tag;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.repository.TagRepo;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Getter
@RequiredArgsConstructor
@TestConfiguration
public class TestTagFactory {

    //  @formatter:off
    private final Random random = new Random();
    @Autowired private TagRepo tagRepo;
    //  @formatter:on

    public List<Tag> generateRandomTag(int count,
                                       User user) {
        List<Tag> tags = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            tags.add(generateRandomTag(user));
        }
        return tags;
    }


    public Tag generateRandomTag(User user) {
        Tag tag = new Tag();
        tag.setName(String.valueOf(random.nextInt(1000)));
        tag.setUser(user);
        return tag;
    }

    public Tag save(Tag tag) {
        return tagRepo.save(tag);
    }

    public List<Tag> saveAll(List<Tag> tags) {
        return StreamSupport
                .stream(tagRepo
                                .saveAll(tags)
                                .spliterator(), false)
                .collect(Collectors.toList());
    }

    public Tag findById(Long id) {
        return tagRepo
                .findById(id)
                .orElse(null);
    }
}
