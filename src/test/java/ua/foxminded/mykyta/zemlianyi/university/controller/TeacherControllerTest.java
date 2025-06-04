package ua.foxminded.mykyta.zemlianyi.university.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import ua.foxminded.mykyta.zemlianyi.university.dto.Course;
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;
import ua.foxminded.mykyta.zemlianyi.university.exceptions.TeacherNotFoundException;
import ua.foxminded.mykyta.zemlianyi.university.service.TeacherService;

@SpringBootTest
@AutoConfigureMockMvc
class TeacherControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TeacherService service;

    Teacher teacher = new Teacher();

    @BeforeEach
    void setUp() {
        teacher.setId(1L);
        teacher.setName("Mykyta");
        teacher.setSurname("Zemlianyi");
        teacher.setEmail("mzeml@gmail.com");

        Course course = new Course();
        course.setId(1L);
        course.setName("IT");

        teacher.addCourse(course);
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void getTeachers_shouldReturnCorrectModel() throws Exception {

        List<Teacher> teachersList = new ArrayList<>();
        teachersList.add(teacher);

        Pageable pageable = PageRequest.of(0, 5);
        Page<Teacher> teachersPage = new PageImpl<Teacher>(teachersList, pageable, teachersList.size());

        when(service.findAll(pageable)).thenReturn(teachersPage);

        mockMvc.perform(get("/teachers").param("page", "0").param("size", "5")).andExpect(status().isOk())
                .andExpect(view().name("view-all-teachers")).andExpect(model().attributeExists("teachers"))
                .andExpect(model().attributeExists("currentPage")).andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attribute("currentPage", 0)).andExpect(model().attribute("totalPages", 1))
                .andExpect(model().attribute("teachers", teachersPage));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void showCreateTeacherForm_shouldReturnModelWithNewTeacher() throws Exception {
        mockMvc.perform(get("/teachers/add")).andExpect(status().isOk()).andExpect(view().name("add-new-teacher"))
                .andExpect(model().attributeExists("teacher"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createTeacher_shouldRedirectWithSuccess_whenCreatedValidTeacher() throws Exception {
        mockMvc.perform(post("/teachers/add").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Marek").param("surname", "Szepski").param("email", "mszepski@gmail.com")
                .param("password", "12345").param("group", "1")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teachers"))
                .andExpect(flash().attribute("successMessage", "Teacher added successfully!"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createTeacher_shouldRedirectWithError_whenServiceThrowsException() throws Exception {
        Teacher newTeacher = new Teacher();
        newTeacher.setName("Mykyta");
        newTeacher.setSurname("Zemlianyi");
        newTeacher.setEmail("mzeml@gmail.com");
        newTeacher.setPassword("12345");

        doThrow(new IllegalArgumentException("Service error")).when(service).addNew(newTeacher);

        mockMvc.perform(post("/teachers/add").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", newTeacher.getName()).param("surname", newTeacher.getSurname())
                .param("email", newTeacher.getEmail()).param("password", newTeacher.getPassword()))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/teachers"))
                .andExpect(flash().attribute("errorMessage", "Error: Service error"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createTeacher_shouldReturnWithErrors_whenBindingExceptionOccurs() throws Exception {
        Teacher newTeacher = new Teacher();
        newTeacher.setName(" ");
        newTeacher.setSurname("Zemlianyi");
        newTeacher.setEmail("mzeml@gmail.com");
        newTeacher.setPassword("12345");

        mockMvc.perform(post("/teachers/add").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", newTeacher.getName()).param("surname", newTeacher.getSurname())
                .param("email", newTeacher.getEmail()).param("password", newTeacher.getPassword()))
                .andExpect(status().isOk()).andExpect(view().name("add-new-teacher"))
                .andExpect(model().attributeHasErrors("teacher"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void showEditTeacherForm_shouldReturnViewWithTeacher_whenTeacherExists() throws Exception {
        when(service.getByIdOrThrow(1L)).thenReturn(teacher);

        mockMvc.perform(get("/teachers/edit/1")).andExpect(status().isOk()).andExpect(view().name("edit-teacher"))
                .andExpect(model().attribute("teacher", teacher));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void showEditTeacherForm_shouldRedirectWithError_whenServiceFails() throws Exception {
        when(service.getByIdOrThrow(1L)).thenThrow(new TeacherNotFoundException(1L));

        mockMvc.perform(get("/teachers/edit/1")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teachers")).andExpect(flash().attribute("errorMessage",
                        "Error: 1 | Teacher with this ID does not exist in database"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateTeacher_shouldRedirectWithSuccess_whenInputValidFields() throws Exception {

        Teacher modifiedTeacher = new Teacher();
        modifiedTeacher.setId(1L);
        modifiedTeacher.setName("Marek");
        modifiedTeacher.setSurname("Szepski");
        modifiedTeacher.setEmail("mszepski@gmail.com");
        modifiedTeacher.setPassword("12345");

        mockMvc.perform(post("/teachers/edit/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", modifiedTeacher.getName()).param("surname", modifiedTeacher.getSurname())
                .param("email", modifiedTeacher.getEmail()).param("password", modifiedTeacher.getPassword()))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/teachers"))
                .andExpect(flash().attribute("successMessage", "Teacher updated successfully!"));

        verify(service).update(modifiedTeacher);
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateTeacher_shouldRedirectWithError_whenUpdateFails() throws Exception {

        Teacher modifiedTeacher = new Teacher();
        modifiedTeacher.setId(1L);
        modifiedTeacher.setName("Marek");
        modifiedTeacher.setSurname("Szepski");
        modifiedTeacher.setEmail("mszepski@gmail.com");
        modifiedTeacher.setPassword("12345");

        when(service.update(modifiedTeacher)).thenThrow(new IllegalArgumentException("Service Error"));

        mockMvc.perform(post("/teachers/edit/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", modifiedTeacher.getName()).param("surname", modifiedTeacher.getSurname())
                .param("email", modifiedTeacher.getEmail()).param("password", modifiedTeacher.getPassword()))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/teachers"))
                .andExpect(flash().attribute("errorMessage", "Error: Service Error"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateTeacher_shouldReturnWithErrors_whenBindingExceptionOccurs() throws Exception {
        Teacher modifiedTeacher = new Teacher();
        modifiedTeacher.setName(" ");
        modifiedTeacher.setSurname("Zemlianyi");
        modifiedTeacher.setEmail("mzeml@gmail.com");
        modifiedTeacher.setPassword("12345");

        mockMvc.perform(post("/teachers/edit/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", modifiedTeacher.getName()).param("surname", modifiedTeacher.getSurname())
                .param("email", modifiedTeacher.getEmail()).param("password", modifiedTeacher.getPassword()))
                .andExpect(status().isOk()).andExpect(view().name("edit-teacher"))
                .andExpect(model().attributeHasFieldErrors("teacher", "name"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteTeacher_shouldRedirectWithSuccess_whenTeacherExistsInDb() throws Exception {
        when(service.getByIdOrThrow(1L)).thenReturn(teacher);

        mockMvc.perform(delete("/teachers/delete/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/teachers"))
                .andExpect(flash().attribute("successMessage", "Teacher deleted successfully!"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteTeacher_shouldRedirectWithError_whenTeacherDoesNotExistsInDb() throws Exception {
        doThrow(new TeacherNotFoundException(1L)).when(service).deleteOrThrow(1L);

        mockMvc.perform(delete("/teachers/delete/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/teachers")).andExpect(flash()
                        .attribute("errorMessage", "Error: 1 | Teacher with this ID does not exist in database"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteTeacher_shouldRedirectWithError_whenServiceFails() throws Exception {
        doThrow(new IllegalArgumentException("Service error")).when(service).deleteOrThrow(1L);

        mockMvc.perform(delete("/teachers/delete/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/teachers"))
                .andExpect(flash().attribute("errorMessage", "Error: Service error"));
    }

}
