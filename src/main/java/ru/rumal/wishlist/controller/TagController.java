package ru.rumal.wishlist.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.rumal.wishlist.facade.TagFacade;
import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.EventDto;
import ru.rumal.wishlist.model.dto.TagDto;
import ru.rumal.wishlist.model.dto.View;

import java.security.Principal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/tag")
@RestController
public class TagController {

    private final TagFacade tagFacade;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(View.Response.class)
    public ResponseEntity<BaseDto> create(Principal principal,
                                          @Validated(View.New.class) @RequestBody TagDto tagDto) {
        BaseDto response = tagFacade.create(principal, tagDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(View.Response.class)
    public ResponseEntity<List<BaseDto>> getAll(Principal principal) {
        List<BaseDto> response = tagFacade.getAll(principal);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(path = "/basic", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(View.Response.class)
    public ResponseEntity<List<BaseDto>> getBasic() {
        List<BaseDto> response = tagFacade.getAllBasic();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping(path = "/{id}")
    @JsonView(View.Response.class)
    public ResponseEntity<BaseDto> update(Principal principal,
                                          @PathVariable Long id,
                                          @Validated(View.Update.class) @RequestBody TagDto tagDto) {
        BaseDto response = tagFacade.update(principal, id, tagDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Long> delete(Principal principal,
                                       @PathVariable Long id) {
        Long response = tagFacade.delete(principal, id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}