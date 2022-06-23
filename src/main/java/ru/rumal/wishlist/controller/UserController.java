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
import java.security.Principal;

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
    public ResponseEntity<BaseDto> info(Principal principal) {
        BaseDto response = userFacade.getInfo(principal);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping
    public ResponseEntity<String> delete(Principal principal,
                                         HttpServletRequest request) throws ServletException {
        String response = userFacade.delete(principal);
        request.logout();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping
    @JsonView(View.Response.class)
    public ResponseEntity<BaseDto> updateInfo(Principal principal,
                                          @Validated(View.Update.class) @RequestBody UserDto userDto) {
        BaseDto response = userFacade.updateInfo(principal, userDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/password")
    @JsonView(View.Response.class)
    public ResponseEntity<BaseDto> updatePassword(Principal principal,
                                                  @Validated(View.UpdatePassword.class) @RequestBody UserDto userDto) {
        BaseDto response = userFacade.updatePassword(principal, userDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
