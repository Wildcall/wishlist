package ru.rumal.wishlist.integration;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.rumal.wishlist.integration.context.WithMockAppUser;
import ru.rumal.wishlist.integration.utils.TestUserFactory;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.repository.UserRepo;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.rumal.wishlist.integration.utils.HttpResponseApiError.isApiError;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@Import(TestUserFactory.class)
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserDeleteTest {

    @Autowired
    private TestUserFactory testUserFactory;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepo userRepo;

    @DisplayName("Auto logout and delete user from db")
    @WithMockAppUser
    @Test
    public void deleteUserWhenAuthenticated() throws Exception {
        testUserFactory.initDB();

        this.mockMvc
                .perform(delete("/api/v1/user").with(csrf()))
                .andDo(print())
                .andExpectAll(status().isOk());

        this.mockMvc
                .perform(get("/api/v1/user").with(csrf()))
                .andDo(print())
                .andExpect(status().isForbidden());

        User correctUser = testUserFactory.getCorrectUser();
        User byEmail = userRepo
                .findByEmail(correctUser.getEmail())
                .orElse(null);
        Assertions.assertNull(byEmail);

        testUserFactory.clearDB();
    }

    @DisplayName("Return api error when not authenticated")
    @Test
    public void deleteReturnApiErrorWhenNotAuthenticated() throws Exception {
        this.mockMvc
                .perform(delete("/api/v1/user").with(csrf()))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpectAll(isApiError("Access denied", "FORBIDDEN"));
    }
}
