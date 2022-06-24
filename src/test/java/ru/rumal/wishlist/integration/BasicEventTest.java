package ru.rumal.wishlist.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.rumal.wishlist.integration.context.WithMockAppUser;
import ru.rumal.wishlist.integration.utils.HttpRequestBuilder;
import ru.rumal.wishlist.model.entity.BasicEvent;
import ru.rumal.wishlist.repository.BasicEventRepo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.rumal.wishlist.integration.utils.HttpResponseApiError.isApiError;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BasicEventTest {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final String[] nameList = {"Новый год", "МЖД", "День программиста"};
    private final String[] dateList = {"2023-01-01 00:00", "2023-08-03 15:00", "2022-09-13 15:00"};
    @Autowired
    private BasicEventRepo basicEventRepo;
    @Autowired
    private MockMvc mockMvc;
  
    @DisplayName("Pass if all endpoint is secure when not auth")
    @Test
    @Order(1)
    public void checkApiSecure() {
        Stream
                .of(get("/api/v1/basic_event"), post("/api/v1/basic_event"), delete("/api/v1/basic_event/1"))
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

    @DisplayName("Pass if all endpoint is secure when role not ADMIN")
    @Test
    @WithMockAppUser
    @Order(2)
    public void checkApiSecureWhenUser() {
        Stream
                .of(get("/api/v1/basic_event"), post("/api/v1/basic_event"), delete("/api/v1/basic_event/1"))
                .forEach(a -> {
                    try {
                        this.mockMvc
                                .perform(a.with(csrf()))
                                .andDo(print())
                                .andExpect(status().isUnauthorized())
                                .andExpectAll(isApiError("Access is denied", "UNAUTHORIZED"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    @DisplayName("Return basic event")
    @WithMockAppUser(role = "ADMIN")
    @Test
    @Order(3)
    public void createReturnEvent() throws Exception {

        Map<String, String> eventMap = new HashMap<String, String>() {{
            put("name", nameList[0]);
            put("date", dateList[0]);
        }};

        this.mockMvc
                .perform(HttpRequestBuilder.postJson("/api/v1/basic_event", eventMap))
                .andDo(print())
                .andExpectAll(status().isCreated(),
                              content().contentType("application/json"),
                              jsonPath("$.name", is(nameList[0])),
                              jsonPath("$.date", is(dateList[0])));

        Optional<BasicEvent> optionalEvent = basicEventRepo.findById(1L);

        Assertions.assertTrue(optionalEvent.isPresent());
        BasicEvent event = optionalEvent.get();
        LocalDateTime dateTime = LocalDateTime.parse(dateList[0], formatter);
        Assertions.assertEquals(event.getId(), 1);
        Assertions.assertEquals(event.getName(), nameList[0]);
        Assertions.assertEquals(event.getDate(), dateTime);
        basicEventRepo.deleteAll();
    }

    @DisplayName("Return list of basic event")
    @WithMockAppUser(role = "ADMIN")
    @Test
    @Order(4)
    public void getAllWhenExists() throws Exception {
        for (int i = 0; i < nameList.length; i++) {
            BasicEvent basicEvent = new BasicEvent(null,
                                                   nameList[i],
                                                   LocalDateTime.parse(dateList[i], formatter));
            basicEventRepo.save(basicEvent);
        }

        this.mockMvc
                .perform(HttpRequestBuilder.getJson("/api/v1/basic_event"))
                .andDo(print())
                .andExpectAll(status().isOk(),
                              content().contentType("application/json"),
                              jsonPath("$[*].id", is(notNullValue())),
                              jsonPath("$[*].name", containsInAnyOrder(nameList)),
                              jsonPath("$[*].date", containsInAnyOrder(dateList)));
    }
}
