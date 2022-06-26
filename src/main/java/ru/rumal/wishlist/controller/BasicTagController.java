package ru.rumal.wishlist.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.rumal.wishlist.facade.BasicTagFacade;
import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.BasicTagDto;
import ru.rumal.wishlist.model.dto.View;

import java.security.Principal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/basic_tag")
@RestController
public class BasicTagController {
    private final BasicTagFacade basicTagFacade;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(View.Response.class)
    public ResponseEntity<BaseDto> create(@Validated(View.New.class) @RequestBody BasicTagDto basicTagDto) {
        BaseDto response = basicTagFacade.create(basicTagDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(View.Response.class)
    public ResponseEntity<List<BaseDto>> getAll(Principal principal) {
        List<BaseDto> response = basicTagFacade.getAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Long> delete(Principal principal,
                                       @PathVariable Long id) {
        Long response = basicTagFacade.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
