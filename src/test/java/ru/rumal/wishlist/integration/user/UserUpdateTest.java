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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.rumal.wishlist.integration.context.WithMockAppUser;
import ru.rumal.wishlist.integration.utils.HttpRequestBuilder;
import ru.rumal.wishlist.integration.utils.TestUserFactory;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.rumal.wishlist.integration.utils.HttpResponseApiError.isApiError;
import static ru.rumal.wishlist.integration.utils.TestUserFactory.UserUpdateInfoRequest;
import static ru.rumal.wishlist.integration.utils.TestUserFactory.UserUpdatePasswordRequest;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@Import(TestUserFactory.class)
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class UserUpdateTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("Update info return api error 'not impl'")
    @WithMockAppUser
    @Test
    public void updateInfo() throws Exception {
        UserUpdateInfoRequest request = new UserUpdateInfoRequest();
        request.setEmail("new_email@mail.com");
        request.setName("New name");

        this.mockMvc
                .perform(HttpRequestBuilder.putJson("/api/v1/user", request))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpectAll(isApiError("Not impl", "BAD_REQUEST"));
    }

    @DisplayName("Update password return api error 'not impl'")
    @WithMockAppUser
    @Test
    public void updatePassword() throws Exception {
        UserUpdatePasswordRequest request = new UserUpdatePasswordRequest();
        request.setPassword("oldPassword");
        request.setNewPassword("newPassword");

        this.mockMvc
                .perform(HttpRequestBuilder.putJson("/api/v1/user/password", request))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpectAll(isApiError("Not impl", "BAD_REQUEST"));
    }
}