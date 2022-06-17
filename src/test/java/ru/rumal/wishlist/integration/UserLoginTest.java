package ru.rumal.wishlist.integration;

import lombok.RequiredArgsConstructor;
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
import ru.rumal.wishlist.integration.utils.TestUserFactory;
import ru.rumal.wishlist.model.Role;
import ru.rumal.wishlist.model.entity.User;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.rumal.wishlist.integration.utils.HttpResponseApiError.isApiError;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@Import(TestUserFactory.class)
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserLoginTest {

    @Autowired
    private TestUserFactory testUserFactory;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("Return user info when credentials is valid")
    @Test
    public void loginWithCorrectCredentials() throws Exception {
        testUserFactory.initDB();
        User correctUser = testUserFactory.getCorrectUser();
        this.mockMvc
                .perform(post("/api/v1/auth/login")
                                 .contentType("application/x-www-form-urlencoded")
                                 .param("email", testUserFactory
                                         .getCorrectEmail())
                                 .param("password", testUserFactory
                                         .getCorrectPasswordDecrypt())
                                 .with(csrf()))
                .andDo(print())
                .andExpectAll(status().isOk(),
                              content().contentType("application/json"),
                              jsonPath("$.id", is(correctUser.getId())),
                              jsonPath("$.email", is(correctUser.getEmail())),
                              jsonPath("$.name", is(correctUser.getName())),
                              jsonPath("$.picture", is(correctUser.getPicture())),
                              jsonPath("$.authType", is(correctUser
                                                                .getAuthType()
                                                                .name())),
                              jsonPath("$.role", is(Role.USER.name())),
                              jsonPath("$.password").doesNotExist());
        testUserFactory.clearDB();
    }

    @DisplayName("Return error when credentials is not valid")
    @Test
    public void loginWithBadCredentials() throws Exception {
        this.mockMvc
                .perform(post("/api/v1/auth/login")
                                 .contentType("application/x-www-form-urlencoded")
                                 .param("email", testUserFactory
                                         .getWrongEmail())
                                 .param("password", testUserFactory
                                         .getWrongPassword())
                                 .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpectAll(isApiError("Email or password is invalid", "BAD_REQUEST"));
    }
}
