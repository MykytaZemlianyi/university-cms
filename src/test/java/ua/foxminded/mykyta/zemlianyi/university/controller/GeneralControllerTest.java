package ua.foxminded.mykyta.zemlianyi.university.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import ua.foxminded.mykyta.zemlianyi.university.dto.Admin;
import ua.foxminded.mykyta.zemlianyi.university.dto.Staff;
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;
import ua.foxminded.mykyta.zemlianyi.university.dto.User;
import ua.foxminded.mykyta.zemlianyi.university.service.UserService;
import ua.foxminded.mykyta.zemlianyi.university.service.UserServiceResolver;

@SpringBootTest
@AutoConfigureMockMvc
class GeneralControllerTest {

    @MockitoBean
    UserServiceResolver userServiceResolver;

    private UserService<User> userService = Mockito.mock(UserService.class);;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void index_shouldReturnIndexView() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index"));
    }

    @Test
    void showLoginPage_shouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/login")).andExpect(status().isOk()).andExpect(view().name("login"));
    }

    @Test
    void welcomePage_shouldReturnWelcomeView_whenUserAutorized() throws Exception {
        mockMvc.perform(get("/welcome").with(user("admin@gmail.com").roles("ADMIN"))).andExpect(status().isOk())
                .andExpect(view().name("welcome"));
    }

    @Test
    void welcomePage_shouldReturnCode302_whenUserDoesNotAutorized() throws Exception {
        mockMvc.perform(get("/welcome")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    private static Stream<Arguments> userRoles() {
        return Stream.of(Arguments.of("admin@gmail.com", "ADMIN", new Admin()),
                Arguments.of("student@gmail.com", "STUDENT", new Student()),
                Arguments.of("teacher@gmail.com", "TEACHER", new Teacher()),
                Arguments.of("staff@gmail.com", "STAFF", new Staff()));
    }

    @ParameterizedTest
    @MethodSource("userRoles")
    void myAccount_shouldReturnViewMyAccount_whenUserAuthorized(String username, String role, User user)
            throws Exception {
        when(userServiceResolver.resolveUserService(role)).thenReturn(userService);

        when(userService.getByEmailOrThrow(username)).thenReturn(user);

        mockMvc.perform(get("/account").with(user(username).roles(role))).andExpect(status().isOk())
                .andExpect(view().name("view-my-account")).andExpect(model().attributeExists("user"));
    }

    @ParameterizedTest
    @MethodSource("userRoles")
    void changePassword_shouldRedirectWithSuccessMessage_whenPasswordChanged(String username, String role, User user)
            throws Exception {

        when(userServiceResolver.resolveUserService(role)).thenReturn(userService);
        doNothing().when(userService).changePassword(eq(username), any(String.class), any(String.class));

        mockMvc.perform(post("/account/change-password").with(user(username).roles(role))
                .param("currentPassword", "oldPass123").param("newPassword", "newPass456").with(csrf()))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/account"))
                .andExpect(flash().attribute("successMessage", "Password changed successfully!"));

        verify(userService).changePassword(username, "oldPass123", "newPass456");
    }
}
