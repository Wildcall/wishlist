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
import org.springframework.transaction.annotation.Transactional;
import ru.rumal.wishlist.integration.context.WithMockAppUser;
import ru.rumal.wishlist.integration.utils.*;
import ru.rumal.wishlist.model.GiftStatus;
import ru.rumal.wishlist.model.entity.Event;
import ru.rumal.wishlist.model.entity.Gift;
import ru.rumal.wishlist.model.entity.Tag;
import ru.rumal.wishlist.model.entity.User;

import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.rumal.wishlist.integration.utils.HttpResponseApiError.isApiError;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@Import({TestUserFactory.class, TestGiftFactory.class, TestEventFactory.class, TestTagFactory.class, ResultActionUtils.class})
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GiftTest {

    //  @formatter:off
    @Autowired private TestUserFactory userFactory;
    @Autowired private TestGiftFactory giftFactory;
    @Autowired private TestEventFactory eventFactory;
    @Autowired private TestTagFactory tagFactory;
    @Autowired private ResultActionUtils resultActionUtils;
    @Autowired private MockMvc mockMvc;
    //  @formatter:on

    @Value("${limits.gift:20}")
    @Min(1)
    private int giftLimit;
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
                .of(get("/api/v1/gift"), post("/api/v1/gift"), put("/api/v1/gift/1"), delete("/api/v1/gift/1"))
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

    @DisplayName("Create return gift with id")
    @WithMockAppUser
    @Test
    public void createReturnGiftWithId() throws Exception {
        String name = "New gift";

        Map<String, String> giftMap = new HashMap<>();
        giftMap.put("name", name);

        //  @formatter:off
        Long id = resultActionUtils
                .extractLongId(
                        this.mockMvc
                                .perform(HttpRequestBuilder.postJson("/api/v1/gift", giftMap))
                                .andDo(print())
                                .andExpectAll(status().isCreated(),
                                              content().contentType("application/json"),
                                              jsonPath("$.id").hasJsonPath(),
                                              jsonPath("$.name", is(name)),
                                              jsonPath("$.link").hasJsonPath(),
                                              jsonPath("$.picture").hasJsonPath(),
                                              jsonPath("$.description").hasJsonPath(),
                                              jsonPath("$.status", is(GiftStatus.NEW.name())),
                                              jsonPath("$.tagId").hasJsonPath()));

        compareGift(id, user.getId(), null, name, null, null, null, GiftStatus.NEW);
        //  @formatter:on
    }

    @DisplayName("Create return gift with tag bind")
    @WithMockAppUser
    @Test
    public void createReturnGiftWithTagBind() throws Exception {
        Tag tag = tagFactory.save(tagFactory.generateRandomTag(user));

        String name = "New gift";

        Map<String, Object> giftMap = new HashMap<>();
        giftMap.put("name", name);
        giftMap.put("tagId", tag.getId());

        //  @formatter:off
        Long id = resultActionUtils
                .extractLongId(
                        this.mockMvc
                                .perform(HttpRequestBuilder.postJson("/api/v1/gift", giftMap))
                                .andDo(print())
                                .andExpectAll(status().isCreated(),
                                              content().contentType("application/json"),
                                              jsonPath("$.id").hasJsonPath(),
                                              jsonPath("$.name", is(name)),
                                              jsonPath("$.link").hasJsonPath(),
                                              jsonPath("$.picture").hasJsonPath(),
                                              jsonPath("$.description").hasJsonPath(),
                                              jsonPath("$.status", is(GiftStatus.NEW.name())),
                                              jsonPath("$.tagId", is(((Number) tag.getId()).intValue()))));
        //  @formatter:on

        compareGift(id, user.getId(), tag, name, null, null, null, GiftStatus.NEW);
    }

    @DisplayName("Create return gift with event bind")
    @WithMockAppUser
    @Test
    public void createReturnGiftWithEventBind() throws Exception {
        Event event = eventFactory.save(eventFactory.generateRandomEvent(user));

        String name = "New gift";

        Map<String, Object> giftMap = new HashMap<>();
        giftMap.put("name", name);
        giftMap.put("eventId", event.getId());

        //  @formatter:off
        Long id = resultActionUtils
                .extractLongId(
                        this.mockMvc
                                .perform(HttpRequestBuilder.postJson("/api/v1/gift", giftMap))
                                .andDo(print())
                                .andExpectAll(status().isCreated(),
                                              content().contentType("application/json"),
                                              jsonPath("$.id").hasJsonPath(),
                                              jsonPath("$.name", is(name)),
                                              jsonPath("$.link").hasJsonPath(),
                                              jsonPath("$.picture").hasJsonPath(),
                                              jsonPath("$.description").hasJsonPath(),
                                              jsonPath("$.status", is(GiftStatus.NEW.name())),
                                              jsonPath("$.tagId").hasJsonPath()));

        //  @formatter:on
        compareGift(id, user.getId(), null, name, null, null, null, GiftStatus.NEW);
    }

    @DisplayName("Create gift return api error when tag not found")
    @WithMockAppUser
    @Test
    public void createReturnErrorIfTagNotFound() throws Exception {
        String name = "New gift";

        Map<String, Object> giftMap = new HashMap<>();
        giftMap.put("name", name);
        giftMap.put("tagId", Long.MAX_VALUE);
        this.mockMvc
                .perform(HttpRequestBuilder.postJson("/api/v1/gift", giftMap))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpectAll(isApiError("Tag with id: '" + Long.MAX_VALUE + "' not found", "BAD_REQUEST"));
    }

    @DisplayName("Create gift return api error when event not found")
    @WithMockAppUser
    @Test
    public void createGiftReturnErrorWhenEventNotFound() throws Exception {
        String name = "New name";

        Map<String, Object> giftMap = new HashMap<>();
        giftMap.put("name", name);
        giftMap.put("eventId", Long.MAX_VALUE);

        this.mockMvc
                .perform(HttpRequestBuilder.postJson("/api/v1/gift", giftMap))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpectAll(isApiError("Event with id '" + Long.MAX_VALUE + "' not found!", "BAD_REQUEST"));
    }

    @DisplayName("Create return api error when try to create more then ${limits.event} gifts")
    @WithMockAppUser
    @Test
    public void createReturnApiErrorWhenEventsCountMoreThenLimit() throws Exception {
        List<Gift> gifts = giftFactory.generateRandomGift(giftLimit, user);
        giftFactory.saveAll(gifts);

        String name = "New event";

        Map<String, String> giftMap = new HashMap<>();
        giftMap.put("name", name);

        this.mockMvc
                .perform(HttpRequestBuilder.postJson("/api/v1/gift", giftMap))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpectAll(isApiError("You can't create more then " + giftLimit + " gifts", "BAD_REQUEST"));
    }

    @DisplayName("Update gift return updated gift")
    @WithMockAppUser
    @Test
    public void updateGiftReturnUpdatedGift() throws Exception {
        Tag tag = tagFactory.save(tagFactory.generateRandomTag(user));
        Gift gift = giftFactory.save(giftFactory.generateRandomGift(user));

        String name = "New name";
        String link = "New link";
        String picture = "New picture";
        String description = "New description";
        GiftStatus status = GiftStatus.RECEIVED;

        Map<String, Object> giftMap = new HashMap<>();
        giftMap.put("name", name);
        giftMap.put("link", link);
        giftMap.put("picture", picture);
        giftMap.put("description", description);
        giftMap.put("status", status.name());
        giftMap.put("tagId", tag.getId());

        //  @formatter:off
        Long id = resultActionUtils
                .extractLongId(
                        this.mockMvc
                                .perform(HttpRequestBuilder.putJson("/api/v1/gift/" + gift.getId(), giftMap))
                                .andDo(print())
                                .andExpectAll(status().isOk(),
                                              content().contentType("application/json"),
                                              jsonPath("$.id").hasJsonPath(),
                                              jsonPath("$.name", is(name)),
                                              jsonPath("$.link").hasJsonPath(),
                                              jsonPath("$.picture").hasJsonPath(),
                                              jsonPath("$.description").hasJsonPath(),
                                              jsonPath("$.status", is(status.name())),
                                              jsonPath("$.tagId", is(((Number) tag.getId()).intValue()))));

        compareGift(id, user.getId(), tag, name, link, picture, description, status);
        //  @formatter:on
    }

    @DisplayName("Update gift return api error when tag not found")
    @WithMockAppUser
    @Test
    public void updateGiftReturnErrorWhenTagNotFound() throws Exception {
        Gift gift = giftFactory.save(giftFactory.generateRandomGift(user));

        String name = "New name";

        Map<String, Object> giftMap = new HashMap<>();
        giftMap.put("name", name);
        giftMap.put("tagId", Long.MAX_VALUE);

        this.mockMvc
                .perform(HttpRequestBuilder.putJson("/api/v1/gift/" + gift.getId(), giftMap))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpectAll(isApiError("Tag with id: '" + Long.MAX_VALUE + "' not found", "BAD_REQUEST"));
    }

    @DisplayName("Delete return gift id when success")
    @WithMockAppUser
    @Test
    public void deleteReturnLong() throws Exception {
        Gift gift = giftFactory.save(giftFactory.generateRandomGift(user));

        this.mockMvc
                .perform(delete("/api/v1/gift/" + gift.getId()).with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(content().contentType(MediaType.APPLICATION_JSON),
                              content().string(String.valueOf(gift.getId())));

        Assertions.assertNotNull(gift.getId());
        Gift savedGift = giftFactory.getGiftById(gift.getId());
        Assertions.assertNull(savedGift);
    }

    @DisplayName("Delete return api error if gift with expected id not found")
    @WithMockAppUser
    @Test
    public void deleteReturnApiError() throws Exception {
        this.mockMvc
                .perform(delete("/api/v1/gift/" + 1).with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpectAll(isApiError("Gift with id '" + 1 + "' not found", "BAD_REQUEST"));
    }

    @DisplayName("Get all return list of gifts")
    @WithMockAppUser
    @Test
    public void getAllReturnListOfGift() throws Exception {
        List<Gift> gifts = giftFactory.saveAll(giftFactory.generateRandomGift(5, user));

        //  @formatter:off
        this.mockMvc
                .perform(HttpRequestBuilder.getJson("/api/v1/gift"))
                .andDo(print())
                .andExpectAll(status().isOk(),
                              jsonPath("$.size()", is(gifts.size())),
                              jsonPath("$[*].id", containsInAnyOrder(gifts.stream().map(g -> ((Number) g.getId()).intValue()).toArray())),
                              jsonPath("$[*].name", containsInAnyOrder(gifts.stream().map(Gift::getName).toArray())),
                              jsonPath("$[*].link", containsInAnyOrder(gifts.stream().map(Gift::getLink).toArray())),
                              jsonPath("$[*].description", containsInAnyOrder(gifts.stream().map(Gift::getDescription).toArray())),
                              jsonPath("$[*].picture", containsInAnyOrder(gifts.stream().map(Gift::getPicture).toArray())),
                              jsonPath("$[*].status", containsInAnyOrder(gifts.stream().map(Gift::getStatus).map(GiftStatus::name).toArray())),
                              jsonPath("$[*].tagId").hasJsonPath());
        //  @formatter:on
    }

    @Transactional
    public void compareGift(Long giftId,
                             String userId,
                             Tag tag,
                             String name,
                             String link,
                             String picture,
                             String description,
                             GiftStatus status) {
        Gift updatedGift = giftFactory.getGiftById(giftId);
        Assertions.assertNotNull(updatedGift);
        if (userId != null)
            Assertions.assertEquals(updatedGift.getUser().getId(), userId);
        if (tag != null)
            Assertions.assertEquals(updatedGift.getTag(), tag);
        if (name != null)
            Assertions.assertEquals(updatedGift.getName(), name);
        if (link != null)
            Assertions.assertEquals(updatedGift.getLink(), link);
        if (picture != null)
            Assertions.assertEquals(updatedGift.getPicture(), picture);
        if (description != null)
            Assertions.assertEquals(updatedGift.getDescription(), description);
        if (status != null)
            Assertions.assertEquals(updatedGift.getStatus(), status);
    }
}
