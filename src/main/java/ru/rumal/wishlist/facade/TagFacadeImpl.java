package ru.rumal.wishlist.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.rumal.wishlist.exception.BadRequestException;
import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.TagDto;
import ru.rumal.wishlist.model.entity.BasicTag;
import ru.rumal.wishlist.model.entity.Tag;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.service.TagService;

import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class TagFacadeImpl implements TagFacade {

    private final TagService tagService;
    @Value("${limits.tag:10}")
    @Min(1)
    private int tagLimit;

    @Override
    public BaseDto create(Principal principal,
                          TagDto tagDto) {
        String userId = principal.getName();

        if (tagService.getCountByUserId(userId) >= tagLimit)
            throw new BadRequestException("You can't create more then " + tagLimit + " tags");

        Tag tag = (Tag) tagDto.toBaseEntity();
        tag.setUser(new User(userId));

        return tagService
                .save(tag)
                .toBaseDto();
    }

    @Override
    public List<BaseDto> getAll(Principal principal) {
        String userId = principal.getName();

        List<Tag> events = tagService.getAll(userId);
        return events
                .stream()
                .map(Tag::toBaseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Long delete(Principal principal,
                       Long id) {
        String userId = principal.getName();
        if (tagService.deleteByIdAndUserId(id, userId)) return id;
        throw new BadRequestException("Tag not found");
    }

    @Override
    public BaseDto update(Principal principal,
                          Long id,
                          TagDto tagDto) {
        String userId = principal.getName();

        Tag tag = (Tag) tagDto.toBaseEntity();
        return tagService
                .updateByIdAndUserId(id, userId, tag)
                .orElseThrow(() -> new BadRequestException("Tag not found"))
                .toBaseDto();
    }
}
