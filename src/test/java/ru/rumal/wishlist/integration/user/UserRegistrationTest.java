package ru.rumal.wishlist.integration.user;

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
import ru.rumal.wishlist.integration.utils.TestUserFactory;
import ru.rumal.wishlist.model.AuthType;
import ru.rumal.wishlist.model.Role;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.repository.UserRepo;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.rumal.wishlist.integration.utils.HttpRequestBuilder.postJson;
import static ru.rumal.wishlist.integration.utils.HttpResponseApiError.isApiError;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@Import(TestUserFactory.class)
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserRegistrationTest {

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
        testUserFactory.initDB();
    }

    @AfterEach
    public void tearDown() {
        testUserFactory.clearDB();
    }

    @DisplayName("Return new user info, and save in db, when email not exist")
    @Test
    public void registrationWithNewEmail() throws Exception {
        TestUserFactory.UserRegistrationRequest request = new TestUserFactory.UserRegistrationRequest();
        request.setEmail("new_email@mail.com");
        request.setPassword("password");
        request.setName("New User Name");
        User newUser = request.toUser();
        this.mockMvc
                .perform(postJson("/api/v1/user/registration", request))
                .andDo(print())
                .andExpectAll(status().isCreated(),
                              content().contentType(MediaType.APPLICATION_JSON),
                              jsonPath("$.id").hasJsonPath(),
                              jsonPath("$.email", is(request.getEmail())),
                              jsonPath("$.name", is(request.getName())),
                              jsonPath("$.picture").isString(),
                              jsonPath("$.authType", is(AuthType.APPLICATION.name())),
                              jsonPath("$.role", is(Role.USER.name())),
                              jsonPath("$.password").doesNotExist());

        User savedUser = this.userRepo
                .findByEmail(newUser.getEmail())
                .orElse(null);

        Assertions.assertNotNull(savedUser);
        Assertions.assertNotNull(savedUser.getId());
        Assertions.assertEquals(savedUser.getEmail(), newUser.getEmail());
        Assertions.assertTrue(passwordEncoder.matches(newUser.getPassword(), savedUser.getPassword()));
        Assertions.assertEquals(savedUser.getName(), newUser.getName());
        Assertions.assertNotNull(savedUser.getPicture());
        Assertions.assertEquals(savedUser.getAuthType(), AuthType.APPLICATION);
        Assertions.assertEquals(savedUser.getEnable(), newUser.getEnable());
        Assertions.assertEquals(savedUser.getRole(), Role.USER);
    }

    @DisplayName("Return api error, and not sava in db, when email exist")
    @Test
    public void registrationWithAlreadyExistedEmail() throws Exception {
        TestUserFactory.UserRegistrationRequest request = new TestUserFactory.UserRegistrationRequest();
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
}
