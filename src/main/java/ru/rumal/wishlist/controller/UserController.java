package ru.rumal.wishlist.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.rumal.wishlist.facade.UserFacade;
import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.UserDto;
import ru.rumal.wishlist.model.dto.View;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    private final UserFacade userFacade;

    @PostMapping(path = "/registration", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(View.Response.class)
    public ResponseEntity<BaseDto> registration(@Validated(View.New.class) @RequestBody UserDto userDto) {
        BaseDto response = userFacade.registration(userDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(View.Response.class)
    public ResponseEntity<BaseDto> info() {
        BaseDto response = userFacade.getInfo();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping
    public ResponseEntity<String> delete(HttpServletRequest request) throws ServletException {
        String response = userFacade.delete();
        request.logout();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping
    @JsonView(View.Response.class)
    public ResponseEntity<BaseDto> update(@Validated(View.Update.class) @RequestBody UserDto userDto) {
        BaseDto response = userFacade.updateInfo(userDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
