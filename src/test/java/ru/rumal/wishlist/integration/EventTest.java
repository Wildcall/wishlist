package ru.rumal.wishlist.integration;

import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.rumal.wishlist.integration.context.WithMockAppUser;
import ru.rumal.wishlist.integration.utils.HttpRequestBuilder;
import ru.rumal.wishlist.integration.utils.ResultActionUtils;
import ru.rumal.wishlist.integration.utils.TestEventFactory;
import ru.rumal.wishlist.integration.utils.TestUserFactory;
import ru.rumal.wishlist.model.entity.Event;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.repository.EventRepo;

import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.rumal.wishlist.integration.utils.HttpResponseApiError.isApiError;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@Import({TestUserFactory.class, TestEventFactory.class, ResultActionUtils.class})
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class EventTest {

    //  @formatter:off
    @Autowired private TestUserFactory userFactory;
    @Autowired private TestEventFactory eventFactory;
    @Autowired private EventRepo eventRepo;
    @Autowired private ResultActionUtils resultActionUtils;
    @Autowired private MockMvc mockMvc;
    //  @formatter:on
    @Value("${limits.event:20}")
    @Min(1)
    private int eventLimit;
    private User user;

    @BeforeAll
    public void setup() {
        this.user = userFactory.save(userFactory.getCorrectUser());
    }

    @AfterAll
    public void teardown() {
        userFactory.clear(user.getId());
    }

    @DisplayName("Pass if all endpoint is secure")
    @Test
    @Order(1)
    public void checkApiSecure() {
        Stream
                .of(get("/api/v1/event"), post("/api/v1/event"), put("/api/v1/event/1"), delete("/api/v1/event/1"))
                .forEach(a -> {
                    try {
                        this.mockMvc
                                .perform(a.with(csrf()))
                                .andDo(print())
                                .andExpect(status().isForbidden())
                                .andExpectAll(isApiError("Access denied", "FORBIDDEN"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    @DisplayName("Delete return api error if event with expected id not found")
    @WithMockAppUser
    @Test
    @Order(2)
    public void deleteReturnApiError() throws Exception {
        this.mockMvc
                .perform(delete("/api/v1/event/" + 1).with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpectAll(isApiError("Event not found", "BAD_REQUEST"));
    }

    @DisplayName("Create return event with id")
    @WithMockAppUser
    @Test
    @Order(3)
    public void createReturnEventWithId() throws Exception {
        String name = "New event";
        String date = "2022-06-29 10:00";
        String description = "description";

        Map<String, String> eventMap = new HashMap<>();
        eventMap.put("name", name);
        eventMap.put("date", date);
        eventMap.put("description", description);

        //  @formatter:off
        Long id = resultActionUtils.extractLongId(
                this.mockMvc
                        .perform(HttpRequestBuilder.postJson("/api/v1/event", eventMap))
                        .andDo(print())
                        .andExpectAll(status().isCreated(),
                                      content().contentType("application/json"),
                                      jsonPath("$.id").hasJsonPath(),
                                      jsonPath("$.name", is(name)),
                                      jsonPath("$.date", is(date)),
                                      jsonPath("$.description", is(description)),
                                      jsonPath("$.giftsSet").hasJsonPath()));

        Event event = eventFactory.findById(id);
        Assertions.assertNotNull(event);
        Assertions.assertEquals(event.getUser().getId(), user.getId());
        Assertions.assertTrue(event.getGifts().isEmpty());
        //  @formatter:on
    }

    @DisplayName("Delete return event id when success")
    @WithMockAppUser
    @Test
    @Order(4)
    public void deleteReturnLong() throws Exception {

        Event event = eventFactory.save(eventFactory.generateRandomEvent(user));

        this.mockMvc
                .perform(delete("/api/v1/event/" + event.getId()).with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(content().contentType(MediaType.APPLICATION_JSON),
                              content().string(String.valueOf(event.getId())));

        Assertions.assertNotNull(event.getId());
        Optional<Event> id = eventRepo.findById(event.getId());
        Assertions.assertFalse(id.isPresent());
    }

    @DisplayName("Create return api error when try to create more then ${limits.event} events")
    @WithMockAppUser
    @Test
    @Order(5)
    public void createReturnApiErrorWhenEventsCountMoreThenLimit() throws Exception {
        eventFactory.saveAll(eventFactory.generateRandomEvent(20, user));

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
                .andExpect(status().isBadRequest())
                .andExpectAll(isApiError("You can't create more then " + eventLimit + " events", "BAD_REQUEST"));
    }
}
