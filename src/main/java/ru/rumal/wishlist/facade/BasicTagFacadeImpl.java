package ru.rumal.wishlist.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rumal.wishlist.exception.BadRequestException;
import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.BasicTagDto;
import ru.rumal.wishlist.model.entity.BasicTag;
import ru.rumal.wishlist.service.BasicTagService;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class BasicTagFacadeImpl implements BasicTagFacade {
    private final BasicTagService basicTagService;

    @Override
    public BaseDto create(BasicTagDto basicTagDto) {
        BasicTag basicEvent = (BasicTag) basicTagDto.toBaseEntity();
        return basicTagService
                .save(basicEvent)
                .toBaseDto();
    }

    @Override
    public List<BaseDto> getAll() {
        List<BasicTag> events = basicTagService.getAll();
        return events
                .stream()
                .map(BasicTag::toBaseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Long delete(Long id) {
        if (basicTagService.deleteById(id)) return id;
        throw new BadRequestException("Basic tag not found");
    }
}
