package ru.rumal.wishlist.integration.event;

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
import ru.rumal.wishlist.model.entity.Event;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.repository.EventRepo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.rumal.wishlist.integration.utils.HttpResponseApiError.isApiError;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@Import(TestUserFactory.class)
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class EventCreateTest {

    @Autowired
    private TestUserFactory testUserFactory;

    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("Return api error when user not authenticated")
    @Test
    public void infoReturnApiErrorWhenNotAuthenticated() throws Exception {
        this.mockMvc
                .perform(get("/api/v1/event"))
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

        String name = "New event";
        String date = "2022-06-29 10:00";
        String description = "description";

        Map<String, String> eventMap = new HashMap<>();
        eventMap.put("name", name);
        eventMap.put("date", date);
        eventMap.put("description", description);

        this.mockMvc
                .perform(HttpRequestBuilder.postJson("/api/v1/event", eventMap))
                .andDo(print())
                .andExpectAll(status().isCreated(),
                              content().contentType("application/json"),
                              jsonPath("$.id", is(1)),
                              jsonPath("$.name", is(name)),
                              jsonPath("$.date", is(date)),
                              jsonPath("$.description", is(description)),
                              jsonPath("$.giftsSet").hasJsonPath());

        Optional<Event> optionalEvent = eventRepo.findById(1L);

        Assertions.assertTrue(optionalEvent.isPresent());
        Event event = optionalEvent.get();

        Assertions.assertEquals(event.getUser().getId(), user.getId());
        Assertions.assertTrue(event.getGifts().isEmpty());

        testUserFactory.clearDB();
    }

    @DisplayName("Return api error when try to create more then 20 events")
    @WithMockAppUser
    @Test
    public void createReturnApiErrorWhenEventsCountMoreThen20() throws Exception {
        testUserFactory.initDB();
        User user = testUserFactory.getCorrectUser();

        String name = "New event";
        String date = "2022-06-29 10:00";
        String description = "description";

        Map<String, String> eventMap = new HashMap<>();
        eventMap.put("name", name);
        eventMap.put("date", date);
        eventMap.put("description", description);

        this.mockMvc
                .perform(HttpRequestBuilder.postJson("/api/v1/event", eventMap))
                .andDo(print())
                .andExpectAll(status().isCreated(),
                              content().contentType("application/json"),
                              jsonPath("$.id", is(1)),
                              jsonPath("$.name", is(name)),
                              jsonPath("$.date", is(date)),
                              jsonPath("$.description", is(description)),
                              jsonPath("$.giftsSet").hasJsonPath());

        Optional<Event> optionalEvent = eventRepo.findById(1L);

        Assertions.assertTrue(optionalEvent.isPresent());
        Event event = optionalEvent.get();

        Assertions.assertEquals(event.getUser().getId(), user.getId());
        Assertions.assertTrue(event.getGifts().isEmpty());

        testUserFactory.clearDB();
    }
}
