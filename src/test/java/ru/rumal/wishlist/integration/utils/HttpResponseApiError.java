package ru.rumal.wishlist.integration.utils;

import org.springframework.test.web.servlet.ResultMatcher;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class HttpResponseApiError {

    public static ResultMatcher[] isApiError(String message,
                                             String status) {
        return new ResultMatcher[]{
                content().contentType("application/json"),
                jsonPath("$.timestamp", matchesPattern("\\d\\d-\\d\\d-\\d\\d\\d\\d\\s\\d\\d:\\d\\d:\\d\\d")),
                jsonPath("$.status", is(status)),
                jsonPath("$.message", is(message)),
                jsonPath("$.subErrors").hasJsonPath(),
        };
    }
}
