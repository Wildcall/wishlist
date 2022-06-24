package ru.rumal.wishlist.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.rumal.wishlist.integration.context.WithMockAppUser;
import ru.rumal.wishlist.integration.utils.ResultActionUtils;
import ru.rumal.wishlist.integration.utils.TestEventFactory;
import ru.rumal.wishlist.integration.utils.TestGiftFactory;
import ru.rumal.wishlist.integration.utils.TestUserFactory;
import ru.rumal.wishlist.model.GiftStatus;
import ru.rumal.wishlist.model.entity.Event;
import ru.rumal.wishlist.model.entity.Gift;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.service.JwtUtils;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.rumal.wishlist.integration.utils.HttpRequestBuilder.getJson;
import static ru.rumal.wishlist.integration.utils.HttpRequestBuilder.postJson;
import static ru.rumal.wishlist.integration.utils.HttpResponseApiError.isApiError;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@Import({TestUserFactory.class, TestGiftFactory.class, TestEventFactory.class, ResultActionUtils.class})
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReservedTest {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    //  @formatter:off
    @Autowired private TestUserFactory userFactory;
    @Autowired private TestGiftFactory giftFactory;
    @Autowired private TestEventFactory eventFactory;
    @Autowired private JwtUtils jwtUtils;
    @Autowired private MockMvc mockMvc;
    //  @formatter:on
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
                .of(get("/api/v1/reserved/link"), put("/api/v1/reserved/gift"))
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

    @DisplayName("Generate correct link")
    @WithMockAppUser
    @Test
    public void generateLink() throws Exception {
        Event var1 = eventFactory.generateRandomEvent(user);
        Event event = eventFactory.save(var1);

        String token = this.mockMvc
                .perform(get("/api/v1/reserved/link?eventId=" + event.getId()))
                .andDo(print())
                .andExpectAll(status().isOk(),
                              content().contentType(MediaType.APPLICATION_JSON),
                              content().string(any(String.class)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Event var2 = jwtUtils
                .verifyReservedToken(token)
                .orElse(null);
        Assertions.assertNotNull(var2);
        Event var3 = eventFactory.findById(var2.getId());
        Assertions.assertEquals(var3, var1);
    }

    @DisplayName("Return event info with list of gifts")
    @Test
    public void returnEventInfoWithGifts() throws Exception {
        Event var1 = eventFactory.generateRandomEvent(user);
        List<Gift> gifts = giftFactory.saveAll(giftFactory.generateRandomGift(2, user));
        var1.addGift(new HashSet<>(gifts));
        Event event = eventFactory.save(var1);
        String token = jwtUtils.generateReservedToken(user.getId(), event);

        this.mockMvc
                .perform(getJson("/api/v1/reserved/event?token=" + token))
                .andDo(print())
                .andExpectAll(status().isOk(), content().contentType(MediaType.APPLICATION_JSON),
                              jsonPath("$.id", is(((Number) event.getId()).intValue())),
                              jsonPath("$.name", is(event.getName())),
                              jsonPath("$.description", is(event.getDescription())), jsonPath("$.date", is(event
                                                                                                                   .getDate()
                                                                                                                   .format(formatter))),
                              jsonPath("$.giftsSet[*].id", containsInAnyOrder(gifts
                                                                                      .stream()
                                                                                      .map(Gift::getId)
                                                                                      .map(Number::intValue)
                                                                                      .toArray())),
                              jsonPath("$.giftsSet[*].name", containsInAnyOrder(gifts
                                                                                        .stream()
                                                                                        .map(Gift::getName)
                                                                                        .toArray())),
                              jsonPath("$.giftsSet[*].link", containsInAnyOrder(gifts
                                                                                        .stream()
                                                                                        .map(Gift::getLink)
                                                                                        .toArray())),
                              jsonPath("$.giftsSet[*].picture", containsInAnyOrder(gifts
                                                                                           .stream()
                                                                                           .map(Gift::getPicture)
                                                                                           .toArray())),
                              jsonPath("$.giftsSet[*].description", containsInAnyOrder(gifts
                                                                                               .stream()
                                                                                               .map(Gift::getDescription)
                                                                                               .toArray())),
                              jsonPath("$.giftsSet[*].description", containsInAnyOrder(gifts
                                                                                               .stream()
                                                                                               .map(Gift::getDescription)
                                                                                               .toArray())));
    }

    @DisplayName("Return api error if token invalid")
    @Test
    public void returnErrorWhenTokenInvalid() throws Exception {
        Event var1 = eventFactory.generateRandomEvent(user);
        List<Gift> gifts = giftFactory.saveAll(giftFactory.generateRandomGift(2, user));
        var1.addGift(new HashSet<>(gifts));
        Event event = eventFactory.save(var1);
        String token = jwtUtils.generateReservedToken(user.getId(), event);

        this.mockMvc
                .perform(getJson("/api/v1/reserved/event?token=" + token + "q"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpectAll(isApiError("Reserved token not valid", "BAD_REQUEST"));
    }

    @DisplayName("Reserve gift return list of reserved gifts")
    @WithMockAppUser(id = "2", email = "existed@user.com")
    @Test
    public void reserveReturnListOfReservedGifts() throws Exception {
        User holderUser = userFactory.save(userFactory.getExistedUser());

        Event var1 = eventFactory.generateRandomEvent(user);
        List<Gift> gifts = giftFactory.saveAll(giftFactory.generateRandomGift(2, user));
        gifts.forEach(var1::addGift);
        Event event = eventFactory.save(var1);
        String token = jwtUtils.generateReservedToken(user.getId(), event);

        List<Integer> reservedId = new ArrayList<>();
        reservedId.add(((Number) gifts
                .get(0)
                .getId()).intValue());
        reservedId.add(((Number) gifts
                .get(1)
                .getId()).intValue());

        this.mockMvc
                .perform(put("/api/v1/reserved/gift?token=" + token + "&giftsId=" + reservedId.get(
                        0) + "," + reservedId.get(1)).with(csrf()))
                .andDo(print())
                .andExpectAll(status().isOk(), content().contentType(MediaType.APPLICATION_JSON),
                              jsonPath("$[*].id", containsInAnyOrder(reservedId.toArray())));

        Set<Gift> givingGiftsSet = userFactory.getGivingGiftsSet(holderUser.getId());
        Assertions.assertNotNull(givingGiftsSet);
        Assertions.assertFalse(givingGiftsSet.isEmpty());
        for (Gift gift : gifts) {
            Assertions.assertTrue(givingGiftsSet.contains(gift));
        }
        for (Gift gift : givingGiftsSet) {
            Assertions.assertEquals(gift.getStatus(), GiftStatus.RESERVED);
        }
    }
}
