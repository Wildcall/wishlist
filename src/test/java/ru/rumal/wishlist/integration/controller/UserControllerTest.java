package ru.rumal.wishlist.integration.controller;

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
import ru.rumal.wishlist.integration.context.WithMockAppUser;
import ru.rumal.wishlist.integration.utils.TestUserFactory;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.repository.UserRepo;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.rumal.wishlist.integration.utils.HttpRequestBuilder.postJson;
import static ru.rumal.wishlist.integration.utils.HttpResponseApiError.isApiError;
import static ru.rumal.wishlist.integration.utils.TestUserFactory.UserRegistrationRequest;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@Import(TestUserFactory.class)
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class UserControllerTest {

    @Autowired
    private TestUserFactory testUserFactory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    public void setUp() {
        initDb();
    }

    @AfterEach
    public void tearDown() {
        clearDb();
    }

    @DisplayName("Return user info when credentials is valid")
    @Test
    public void loginWithCorrectCredentials() throws Exception {
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
                              jsonPath("$.email", is(correctUser.getEmail())),
                              jsonPath("$.name", is(correctUser.getName())),
                              jsonPath("$.picture", is(correctUser.getPicture())),
                              jsonPath("$.password").doesNotExist());
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

    @DisplayName("Return new user info, and save in db, when email not exist")
    @Test
    public void registrationWithNewEmail() throws Exception {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setEmail("new_email@mail.com");
        request.setPassword("password");
        request.setName("New User Name");
        User newUser = request.toUser();
        this.mockMvc
                .perform(postJson("/api/v1/user/registration", request))
                .andDo(print())
                .andExpectAll(status().isCreated(),
                              content().contentType(MediaType.APPLICATION_JSON),
                              jsonPath("$.email", is(request.getEmail())),
                              jsonPath("$.name", is(request.getName())),
                              jsonPath("$.picture", is("")),
                              jsonPath("$.password").doesNotExist());

        User savedUser = this.userRepo
                .findByEmail(newUser.getEmail())
                .orElse(null);

        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals(savedUser.getEmail(), newUser.getEmail());
        Assertions.assertEquals(savedUser.getName(), newUser.getName());
        Assertions.assertEquals(savedUser.getPicture(), newUser.getPicture());
        Assertions.assertEquals(savedUser.getEnable(), newUser.getEnable());
        Assertions.assertTrue(passwordEncoder.matches(newUser.getPassword(), savedUser.getPassword()));
    }

    @DisplayName("Return api error, and not sava in db, when email exist")
    @Test
    public void registrationWithAlreadyExistedEmail() throws Exception {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setEmail(testUserFactory.getCorrectEmail());
        request.setPassword(testUserFactory.getCorrectPasswordDecrypt());
        request.setName("Existed user");

        this.mockMvc
                .perform(postJson("/api/v1/user/registration", request))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpectAll(isApiError("Email already exist", "BAD_REQUEST"));

        User savedUser = this.userRepo
                .findByEmail(request.getEmail())
                .orElse(null);

        Assertions.assertNotNull(savedUser);
        Assertions.assertNotEquals(savedUser.getName(), request.getName());
    }

    @DisplayName("Return user info without password when user authenticated")
    @WithMockAppUser
    @Test
    public void infoReturnInfoWhenAuthenticated() throws Exception {
        User correctUser = testUserFactory.getCorrectUser();

        this.mockMvc
                .perform(get("/api/v1/user"))
                .andDo(print())
                .andExpectAll(status().isOk(),
                              content().contentType("application/json"),
                              jsonPath("$.email", is(correctUser.getEmail())),
                              jsonPath("$.name", is(correctUser.getName())),
                              jsonPath("$.picture", is(correctUser.getPicture())),
                              jsonPath("$.password").doesNotExist());
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

    @DisplayName("Auto logout and delete user from db")
    @WithMockAppUser
    @Test
    public void deleteUserWhenAuthenticated() throws Exception {
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

    @Test
    public void update() {
    }

    public void initDb() {
        User correctUser = testUserFactory.getCorrectUser();
        this.userRepo.save(correctUser);
    }

    public void clearDb() {
        this.userRepo.deleteAll();
    }
}