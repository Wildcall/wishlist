package ru.rumal.wishlist.integration.basic_event;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.rumal.wishlist.integration.context.WithMockAppUser;
import ru.rumal.wishlist.integration.utils.HttpRequestBuilder;
import ru.rumal.wishlist.integration.utils.TestUserFactory;
import ru.rumal.wishlist.model.entity.BasicEvent;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.repository.BasicEventRepo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static ru.rumal.wishlist.integration.utils.HttpResponseApiError.isApiError;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@Import(TestUserFactory.class)
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class BasicEventTest {

    @Autowired
    private TestUserFactory testUserFactory;

    @Autowired
    private BasicEventRepo basicEventRepo;

    @Autowired
    private MockMvc mockMvc;


    @DisplayName("Return api error when user not authenticated")
    @Test
    public void infoReturnApiErrorWhenNotAuthenticated() throws Exception {
        this.mockMvc
                .perform(get("/api/v1/basic_event"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpectAll(isApiError("Access denied", "FORBIDDEN"));
    }


    @DisplayName("Return event when user authenticated")
    @WithMockAppUser
    @Test
    public void createReturnEventWhenAuthenticated() throws Exception {
        testUserFactory.initDB();
        User user = testUserFactory.getCorrectUser();

        String name = "Новый год";
        String date = "2023-01-01 00:00";

        Map<String, String> eventMap = new HashMap<String, String>() {{
            put("name", "Новый год");
            put("date", "2023-01-01 00:00");
        }};

        this.mockMvc
                .perform(HttpRequestBuilder.postJson("/api/v1/basic_event", eventMap))
                .andDo(print())
                .andExpectAll(status().isCreated(),
                        content().contentType("application/json"),
                        jsonPath("$.name", is(name)),
                        jsonPath("$.date", is(date)));

        Optional<BasicEvent> optionalEvent = basicEventRepo.findById(1L);

        Assertions.assertTrue(optionalEvent.isPresent());
        BasicEvent event = optionalEvent.get();

        LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        Assertions.assertEquals(event.getId(), 1);
        Assertions.assertEquals(event.getName(), name);
        Assertions.assertEquals(event.getDate(), dateTime);

        testUserFactory.clearDB();
    }
}
