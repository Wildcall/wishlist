package ru.rumal.wishlist.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
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
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.repository.UserRepo;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@Import(TestUserFactory.class)
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class UserUpdateTest {

    @Autowired
    private TestUserFactory testUserFactory;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepo userRepo;

    @DisplayName("Return updated user info when authenticated")
    @WithMockAppUser
    @Test
    public void update() throws Exception {
        User user = testUserFactory.getCorrectUser();

        this.mockMvc
                .perform(HttpRequestBuilder.putJson("/api/v1/user", user))
                .andDo(print())
                .andExpectAll(status().isOk(),
                              content().contentType("application/json"));
    }
}