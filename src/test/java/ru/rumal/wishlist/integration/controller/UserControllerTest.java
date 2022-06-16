package ru.rumal.wishlist.integration.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.rumal.wishlist.integration.controller.context.WithMockAppUser;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.repository.UserRepo;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class UserControllerTest {

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

    @Test
    public void login() throws Exception {
        this.mockMvc
                .perform(post("/api/v1/auth/login")
                                 .contentType("application/x-www-form-urlencoded")
                                 .param("email", "test@test.com")
                                 .param("password", "testtest")
                                 .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void registration() {
    }

    @WithMockAppUser(email = "test@test.com")
    @Test
    public void info() throws Exception {
        this.mockMvc
                .perform(get("/api/v1/user"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void delete() {
    }

    @Test
    public void update() {
    }

    public void initDb() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("$2a$12$2FhFNCHhQXC7A3iheMEITuxo9UkhwhTythl6aK2TN2SjBL8EYlIyC"); //testtest
        user.setPicture("");
        user.setEnable(true);
        user.setName("I am test");
        this.userRepo.save(user);
    }

    public void clearDb() {

    }
}