package ru.rumal.wishlist.integration.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.rumal.wishlist.integration.utils.HttpResponseApiError.isApiError;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@TestPropertySource(locations = "classpath:application-test.properties")
class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName(value = "Return index.html with 'auth = false' flag")
    @Test
    public void mainPageWithoutAuthentication() throws Exception {
        this.mockMvc
                .perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString("const auth = false")));
    }

    @DisplayName(value = "Return index.html with 'auth = true' flag")
    @WithMockUser
    @Test
    public void mainPageWithAuthentication() throws Exception {
        this.mockMvc
                .perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString("const auth = true")));
    }

    @DisplayName(value = "Redirect on main page after '/error'")
    @Test
    public void redirectAfterError() throws Exception {
        this.mockMvc
                .perform(get("/error"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @WithMockUser
    @DisplayName(value = "404 status after page not found")
    @Test
    public void correctStatusAfterUnknownPath() throws Exception {
        this.mockMvc
                .perform(get("/unknown_path"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @DisplayName(value = "403 status for unauthorized users")
    @Test
    public void accessDenied() throws Exception {
        this.mockMvc
                .perform(get("/private_page"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpectAll(isApiError("Access denied", "FORBIDDEN"));
    }
}