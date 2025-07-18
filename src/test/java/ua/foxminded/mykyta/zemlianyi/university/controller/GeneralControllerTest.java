package ua.foxminded.mykyta.zemlianyi.university.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class GeneralControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void index_shouldReturnWelcomeView() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("welcome"));
    }

    @Test
    void showLoginPage_shouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/login")).andExpect(status().isOk()).andExpect(view().name("login"));
    }

    @Test
    void welcomePage_shouldReturnWelcomeView_whenUserAutorized() throws Exception {
        mockMvc.perform(get("/").with(user("admin@gmail.com").roles("ADMIN"))).andExpect(status().isOk())
                .andExpect(view().name("welcome"));
    }

    @Test
    void welcomePage_shouldReturnCode302_whenUserDoesNotAutorized() throws Exception {
        mockMvc.perform(get("/welcome")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}
