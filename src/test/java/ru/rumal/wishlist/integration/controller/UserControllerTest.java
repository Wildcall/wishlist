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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.email", is(correctUser.getEmail())))
                .andExpect(jsonPath("$.name", is(correctUser.getName())))
                .andExpect(jsonPath("$.picture", is(correctUser.getPicture())))
                .andExpect(jsonPath("$.password").doesNotExist());
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

    @DisplayName("Registration new user with new email")
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
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email", is(request.getEmail())))
                .andExpect(jsonPath("$.name", is(request.getName())))
                .andExpect(jsonPath("$.picture", is("")))
                .andExpect(jsonPath("$.password").doesNotExist());

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

    @DisplayName("Registration new user with already existed email")
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
    }

    @WithMockAppUser
    @Test
    public void info() throws Exception {
        User correctUser = testUserFactory.getCorrectUser();

        this.mockMvc
                .perform(get("/api/v1/user"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.email", is(correctUser.getEmail())))
                .andExpect(jsonPath("$.name", is(correctUser.getName())))
                .andExpect(jsonPath("$.picture", is(correctUser.getPicture())))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    public void delete() {
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