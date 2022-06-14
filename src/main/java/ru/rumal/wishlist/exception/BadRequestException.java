package ru.rumal.wishlist.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class BadRequestException extends RuntimeException {

    private final ApiError apiError;

    public BadRequestException(String message) {
        super(message);
        this.apiError = new ApiError(HttpStatus.BAD_REQUEST, message);
    }

    public ResponseEntity<Object> getResponse() {
        return new ResponseEntity<>(this.apiError, this.apiError.getStatus());
    }
}
