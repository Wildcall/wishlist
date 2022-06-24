package ru.rumal.wishlist.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rumal.wishlist.facade.ReservedFacade;
import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.View;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/reserved")
@RestController
public class ReservedController {

    private final ReservedFacade reservedFacade;

    @PostMapping(path = ("link"), produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> generateLink(Principal principal,
                                               @RequestBody Long eventId) {
        String response = reservedFacade.generateLink(principal, eventId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(path = "gift", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(View.Response.class)
    public ResponseEntity<Set<BaseDto>> getGifts(@RequestParam String token) {
        Set<BaseDto> response = reservedFacade.getGifts(token);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(path = "event", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(View.Response.class)
    public ResponseEntity<BaseDto> getEvent(@RequestParam String token) {
        BaseDto response = reservedFacade.getEvent(token);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping(path = "gift")
    public ResponseEntity<List<Long>> reserveGift(Principal principal,
                                                  @RequestParam String token,
                                                  @RequestBody List<Long> giftsId) {
        List<Long> response = reservedFacade.reserveGift(principal, giftsId, token);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
