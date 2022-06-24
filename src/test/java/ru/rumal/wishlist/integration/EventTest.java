package ru.rumal.wishlist.integration;

import lombok.RequiredArgsConstructor;
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
import ru.rumal.wishlist.integration.context.WithMockAppUser;
import ru.rumal.wishlist.integration.utils.*;
import ru.rumal.wishlist.model.entity.Event;
import ru.rumal.wishlist.model.entity.Gift;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.repository.EventRepo;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.rumal.wishlist.integration.utils.HttpResponseApiError.isApiError;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@Import({TestUserFactory.class, TestEventFactory.class, TestGiftFactory.class, ResultActionUtils.class})
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EventTest {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    //  @formatter:off
    @Autowired private TestUserFactory userFactory;
    @Autowired private TestEventFactory eventFactory;
    @Autowired private TestGiftFactory giftFactory;
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

    @BeforeEach
    public void eachSetup() {
        eventFactory.clear();
        giftFactory.clear();
    }

    @DisplayName("Pass if all endpoint is secure")
    @Test
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

    @DisplayName("Create return event with id")
    @WithMockAppUser
    @Test
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

        compareEvents(id,
                      user.getId(),
                      name,
                      description,
                      date);
        //  @formatter:on
    }

    @DisplayName("Create return error if date in past")
    @WithMockAppUser
    @Test
    public void createReturnErrorWhenDateInPast() throws Exception {
        String name = "New event";
        String date = LocalDateTime
                .now()
                .minusDays(1)
                .format(formatter);
        String description = "description";

        Map<String, String> eventMap = new HashMap<>();
        eventMap.put("name", name);
        eventMap.put("date", date);
        eventMap.put("description", description);

        //  @formatter:off
        this.mockMvc
                .perform(HttpRequestBuilder.postJson("/api/v1/event", eventMap))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpectAll(isApiError("Date of event must be in future", "BAD_REQUEST"));
        //  @formatter:on
    }

    @DisplayName("Create return api error when try to create more then ${limits.event} events")
    @WithMockAppUser
    @Test
    public void createReturnErrorWhenEventsCountMoreThenLimit() throws Exception {
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

    @DisplayName("Update event return updated event")
    @WithMockAppUser
    @Test
    public void updateReturnUpdatedEvent() throws Exception {
        Event event = eventFactory.save(eventFactory.generateRandomEvent(user));

        String name = "Updated event";
        String description = "Updated description";
        String date = LocalDateTime
                .now()
                .plusDays(1)
                .format(formatter);

        Map<String, String> eventMap = new HashMap<>();
        eventMap.put("name", name);
        eventMap.put("description", description);
        eventMap.put("date", date);
        //  @formatter:off
        this.mockMvc
                .perform(HttpRequestBuilder.putJson("/api/v1/event/" + event.getId(), eventMap))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("$.id", is(((Number) event.getId()).intValue())), jsonPath("$.name", is(name)),
                              jsonPath("$.description", is(description)), jsonPath("$.date", is(date)),
                              jsonPath("$.giftsSet", empty()));


        compareEvents(event.getId(),
                      user.getId(),
                      name,
                      description,
                      date);
        //  @formatter:on
    }

    @DisplayName("Update does not change fields that are not set")
    @WithMockAppUser
    @Test
    public void updateNotChangeNotSetFields() throws Exception {
        Event event = eventFactory.save(eventFactory.generateRandomEvent(user));
        Map<String, String> eventMap = new HashMap<>();

        //  @formatter:off
        String name = "Updated event";
        eventMap.put("name", name);
        this.mockMvc
                .perform(HttpRequestBuilder.putJson("/api/v1/event/" + event.getId(), eventMap))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("$.id", is(((Number) event.getId()).intValue())),
                              jsonPath("$.name", is(name)),
                              jsonPath("$.description", is(event.getDescription())),
                              jsonPath("$.date", is(event
                                                            .getDate()
                                                            .format(formatter))),
                              jsonPath("$.giftsSet", empty()));

        String description = "Updated description";
        eventMap.put("description", description);
        this.mockMvc
                .perform(HttpRequestBuilder.putJson("/api/v1/event/" + event.getId(), eventMap))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("$.id", is(((Number) event.getId()).intValue())),
                              jsonPath("$.name", is(name)),
                              jsonPath("$.description", is(description)),
                              jsonPath("$.date", is(event
                                                            .getDate()
                                                            .format(formatter))),
                              jsonPath("$.giftsSet", empty()));

        String date = LocalDateTime
                .now()
                .plusDays(1)
                .format(formatter);
        eventMap.put("date", date);
        this.mockMvc
                .perform(HttpRequestBuilder.putJson("/api/v1/event/" + event.getId(), eventMap))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("$.id", is(((Number) event.getId()).intValue())),
                              jsonPath("$.name", is(name)),
                              jsonPath("$.description", is(description)),
                              jsonPath("$.date", is(date)),
                              jsonPath("$.giftsSet", empty()));

        compareEvents(event.getId(),
                      user.getId(),
                      name,
                      description,
                      date);
        //  @formatter:on
    }

    @DisplayName("Update event return updated event with bind to gift list")
    @WithMockAppUser
    @Test
    public void updateReturnUpdatedEventWithGiftBind() throws Exception {
        Event event = eventFactory.save(eventFactory.generateRandomEvent(user));

        Set<Gift> gifts = new HashSet<>(giftFactory.saveAll(giftFactory.generateRandomGift(5, user)));

        Set<Integer> giftsId = gifts
                .stream()
                .map(g -> ((Number) g.getId()).intValue())
                .collect(Collectors.toSet());

        String name = "Events with gifts";

        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put("name", name);
        eventMap.put("giftsIdSet", giftsId);

        this.mockMvc
                .perform(HttpRequestBuilder.putJson("/api/v1/event/" + event.getId(), eventMap))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("$.id", is(((Number) event.getId()).intValue())), jsonPath("$.name", is(name)),
                              jsonPath("$.description", is(event.getDescription())), jsonPath("$.date", is(event
                                                                                                                   .getDate()
                                                                                                                   .format(formatter))),
                              jsonPath("$.giftsSet.size()", is(gifts.size())));

        giftsId.add(Integer.MAX_VALUE);

        this.mockMvc
                .perform(HttpRequestBuilder.putJson("/api/v1/event/" + event.getId(), eventMap))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpectAll(isApiError("Gift with id: '" + Integer.MAX_VALUE + "' not found", "BAD_REQUEST"));

        Event updatedEvent = eventFactory.findById(event.getId());
        Assertions.assertNotNull(updatedEvent);
        Assertions.assertEquals(gifts, updatedEvent.getGifts());
    }

    @DisplayName("Update return error if date in past")
    @WithMockAppUser
    @Test
    public void updateReturnErrorWhenDateInPast() throws Exception {
        Event event = eventFactory.save(eventFactory.generateRandomEvent(user));
        String name = "New event";
        String date = LocalDateTime
                .now()
                .minusDays(1)
                .format(formatter);

        Map<String, String> eventMap = new HashMap<>();
        eventMap.put("name", name);
        eventMap.put("date", date);

        //  @formatter:off
        this.mockMvc
                .perform(HttpRequestBuilder.putJson("/api/v1/event/" + event.getId(), eventMap))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpectAll(isApiError("Date of event must be in future", "BAD_REQUEST"));
        //  @formatter:on
    }

    @DisplayName("Delete return event id when success")
    @WithMockAppUser
    @Test
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

    @DisplayName("Delete return error if event not found")
    @WithMockAppUser
    @Test
    public void deleteReturnErrorWhenEventNotFound() throws Exception {
        this.mockMvc
                .perform(delete("/api/v1/event/" + 1).with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpectAll(isApiError("Event with id '" + 1 + "' not found!", "BAD_REQUEST"));
    }

    private void compareEvents(Long eventId,
                               String userId,
                               String name,
                               String description,
                               String date) {
        Event existedEvent = eventFactory.findById(eventId);

        Assertions.assertNotNull(existedEvent);
        Assertions.assertEquals(eventId, existedEvent.getId());
        if (name != null) Assertions.assertEquals(name, existedEvent.getName());
        if (description != null) Assertions.assertEquals(description, existedEvent.getDescription());
        if (date != null) Assertions.assertEquals(date, existedEvent
                .getDate()
                .format(formatter));
        if (userId != null) Assertions.assertEquals(userId, existedEvent
                .getUser()
                .getId());
    }
}
