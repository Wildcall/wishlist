package ru.rumal.wishlist.integration.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.rumal.wishlist.integration.context.WithMockAppUser;
import ru.rumal.wishlist.integration.utils.TestUserFactory;
import ru.rumal.wishlist.model.Role;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.repository.UserRepo;

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
public class UserInfoTest {

    @Autowired
    private TestUserFactory testUserFactory;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("Return user info without password when user authenticated")
    @WithMockAppUser
    @Test
    public void infoReturnInfoWhenAuthenticated() throws Exception {
        testUserFactory.initDB();
        User correctUser = testUserFactory.getCorrectUser();

        this.mockMvc
                .perform(get("/api/v1/user"))
                .andDo(print())
                .andExpectAll(status().isOk(),
                              content().contentType("application/json"),
                              jsonPath("$.id", is(correctUser.getId())),
                              jsonPath("$.email", is(correctUser.getEmail())),
                              jsonPath("$.name", is(correctUser.getName())),
                              jsonPath("$.picture", is(correctUser.getPicture())),
                              jsonPath("$.authType", is(correctUser.getAuthType().name())),
                              jsonPath("$.role", is(Role.USER.name())),
                              jsonPath("$.password").doesNotExist());
        testUserFactory.clearDB();
    }

    @DisplayName("Return api error when user not authenticated")
    @Test
    public void infoReturnApiErrorWhenNotAuthenticated() throws Exception {
        this.mockMvc
                .perform(get("/api/v1/user"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpectAll(isApiError("Access denied", "FORBIDDEN"));
    }
}
