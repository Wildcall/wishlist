package ru.rumal.wishlist.integration.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

public class HttpRequestBuilder {

    private static final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public static MockHttpServletRequestBuilder postJson(String uri,
                                                         Object o) {
        String json = mapper.writeValueAsString(o);
        return post(uri)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
    }

    @SneakyThrows
    public static MockHttpServletRequestBuilder putJson(String uri,
                                                         Object o) {
        String json = mapper.writeValueAsString(o);
        return put(uri)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
    }
}
