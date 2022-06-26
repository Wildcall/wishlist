package ru.rumal.wishlist.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.rumal.wishlist.integration.context.WithMockAppUser;
import ru.rumal.wishlist.integration.utils.HttpRequestBuilder;
import ru.rumal.wishlist.integration.utils.ResultActionUtils;
import ru.rumal.wishlist.integration.utils.TestUserFactory;
import ru.rumal.wishlist.model.AuthType;
import ru.rumal.wishlist.model.Role;
import ru.rumal.wishlist.model.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.rumal.wishlist.integration.utils.HttpRequestBuilder.postJson;
import static ru.rumal.wishlist.integration.utils.HttpResponseApiError.isApiError;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@Import({TestUserFactory.class, ResultActionUtils.class})
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class UserTest {

    //  @formatter:off
    @Autowired private TestUserFactory userFactory;
    @Autowired private ResultActionUtils resultActionUtils;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private MockMvc mockMvc;
    //  @formatter:on
    private User correctUser;
    private User existedUser;

    @BeforeAll
    public void setup() {
        this.correctUser = userFactory.save(userFactory.getCorrectUser());
        this.existedUser = userFactory.save(userFactory.getExistedUser());
    }

    @AfterAll
    public void teardown() {
        userFactory.clear(correctUser.getId());
        userFactory.clear(existedUser.getId());
    }

    @DisplayName("Pass if all endpoint is secure")
    @Test
    public void checkApiSecure() {
        Stream
                .of(get("/api/v1/user"), put("/api/v1/user"), put("/api/v1/user/password"), delete("/api/v1/user"))
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

    @DisplayName("Return new user info, and save in db, when email not exist")
    @Test
    public void registrationWithNewEmail() throws Exception {
        User newUser = userFactory.getNewUser();

        Map<String, String> regMap = new HashMap<>();
        regMap.put("email", newUser.getEmail());
        regMap.put("password", userFactory.getPassDecrypt());
        regMap.put("name", newUser.getName());

        //  @formatter:off
        String id = resultActionUtils
                .extractStringId(
                        this.mockMvc
                                .perform(postJson("/api/v1/user/registration", regMap))
                                .andDo(print())
                                .andExpectAll(status().isCreated(),
                                              content().contentType(
                                                      MediaType.APPLICATION_JSON),
                                              jsonPath("$.id").hasJsonPath(),
                                              jsonPath("$.email", is(newUser.getEmail())),
                                              jsonPath("$.name", is(newUser.getName())),
                                              jsonPath("$.picture").isString(),
                                              jsonPath("$.authType",
                                                       is(AuthType.APPLICATION.name())),
                                              jsonPath("$.role", is(Role.USER.name())),
                                              jsonPath("$.password").doesNotExist()));
        //  @formatter:on

        User savedUser = userFactory.findById(id);

        Assertions.assertNotNull(savedUser);
        Assertions.assertNotNull(savedUser.getId());
        Assertions.assertEquals(savedUser.getEmail(), newUser.getEmail());
        Assertions.assertTrue(passwordEncoder.matches(userFactory.getPassDecrypt(), savedUser.getPassword()));
        Assertions.assertEquals(savedUser.getName(), newUser.getName());
        Assertions.assertNotNull(savedUser.getPicture());
        Assertions.assertEquals(savedUser.getAuthType(), AuthType.APPLICATION);
        Assertions.assertEquals(savedUser.getEnable(), newUser.getEnable());
        Assertions.assertEquals(savedUser.getRole(), Role.USER);
    }

    @DisplayName("Return api error, and not sava in db, when email exist")
    @Test
    public void registrationWithAlreadyExistedEmail() throws Exception {
        Map<String, String> regMap = new HashMap<>();

        regMap.put("email", existedUser.getEmail());
        regMap.put("password", userFactory.getPassDecrypt());
        regMap.put("name", "New User");

        this.mockMvc
                .perform(postJson("/api/v1/user/registration", regMap))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpectAll(isApiError("Email already exist", "BAD_REQUEST"));

        User savedUser = userFactory.findById(existedUser.getId());

        Assertions.assertNotNull(savedUser);
        Assertions.assertNotEquals(savedUser.getName(), regMap.get("name"));
    }

    @DisplayName("Return user info when credentials is valid")
    @Test
    public void loginWithCorrectCredentials() throws Exception {
        this.mockMvc
                .perform(post("/api/v1/auth/login")
                                 .contentType("application/x-www-form-urlencoded")
                                 .param("email", correctUser.getEmail())
                                 .param("password", userFactory.getPassDecrypt())
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
                              jsonPath("$.role", is(correctUser
                                                            .getRole()
                                                            .name())),
                              jsonPath("$.password").doesNotExist());
    }

    @DisplayName("Return error when credentials is not valid")
    @Test
    public void loginWithBadCredentials() throws Exception {
        this.mockMvc
                .perform(post("/api/v1/auth/login")
                                 .contentType("application/x-www-form-urlencoded")
                                 .param("email", "notexisted@user.com")
                                 .param("password", "12345678")
                                 .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpectAll(isApiError("Email or password is invalid", "BAD_REQUEST"));
    }

    @DisplayName("Return user info without password when user authenticated")
    @WithMockAppUser
    @Test
    public void infoReturnInfoWhenAuthenticated() throws Exception {
        this.mockMvc
                .perform(get("/api/v1/user"))
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
                              jsonPath("$.role", is(correctUser
                                                            .getRole()
                                                            .name())),
                              jsonPath("$.password").doesNotExist());
    }

    @DisplayName("Update info return api error 'not impl'")
    @WithMockAppUser
    @Test
    public void updateInfo() throws Exception {
        Map<String, String> regMap = new HashMap<>();
        regMap.put("email", correctUser.getEmail());
        regMap.put("name", "Correct New Name");

        this.mockMvc
                .perform(HttpRequestBuilder.putJson("/api/v1/user", regMap))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpectAll(isApiError("Not impl", "BAD_REQUEST"));
    }

    @DisplayName("Update password return api error 'not impl'")
    @WithMockAppUser
    @Test
    public void updatePassword() throws Exception {
        Map<String, String> regMap = new HashMap<>();
        regMap.put("password", userFactory.getPassDecrypt());
        regMap.put("newPassword", "new12345678");

        this.mockMvc
                .perform(HttpRequestBuilder.putJson("/api/v1/user/password", regMap))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpectAll(isApiError("Not impl", "BAD_REQUEST"));
    }

    @DisplayName("Auto logout and delete user from db")
    @WithMockAppUser(email = "temporal@user.com", id = "temporal")
    @Test
    public void deleteUserWhenAuthenticated() throws Exception {
        User tmpUser = userFactory.save(userFactory.getTmpUser());

        this.mockMvc
                .perform(delete("/api/v1/user").with(csrf()))
                .andDo(print())
                .andExpectAll(status().isOk());

        this.mockMvc
                .perform(get("/api/v1/user").with(csrf()))
                .andDo(print())
                .andExpect(status().isForbidden());

        User deletedUser = userFactory.findById(tmpUser.getId());
        Assertions.assertNull(deletedUser);
    }

}
