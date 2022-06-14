package ru.rumal.wishlist.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApiError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final LocalDateTime timestamp;
    private HttpStatus status;
    private String message;
    private List<ApiValidationError> subErrors;

    private ApiError() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus status,
                    String message) {
        this();
        this.status = status;
        this.message = message;
    }

    public static void writeApiError(HttpServletResponse response,
                                     ObjectMapper mapper,
                                     ApiError apiError) throws IOException {
        mapper.writeValue(response.getOutputStream(), apiError);
    }
}
