package ru.rumal.wishlist.integration.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.rumal.wishlist.controller.EventController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@TestPropertySource(locations = "classpath:application-test.properties")
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventController eventController;

    @Test
    public void test() throws Exception {
        this.mockMvc
                .perform(get("/api/v1/event"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}