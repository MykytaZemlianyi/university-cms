package ua.foxminded.mykyta.zemlianyi.university.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
import ua.foxminded.mykyta.zemlianyi.university.dto.Group;
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;
import ua.foxminded.mykyta.zemlianyi.university.exceptions.CourseNotFoundException;
import ua.foxminded.mykyta.zemlianyi.university.service.CourseService;

@SpringBootTest
@AutoConfigureMockMvc
class CourseControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CourseService service;

    Course course = new Course();

    @BeforeEach
    void setUp() {
        Teacher teacher = new Teacher();
        teacher.setName("Marek");
        teacher.setSurname("Szepski");

        Group group = new Group();
        group.setName("AA-11");

        course.setId(1L);
        course.setName("Math");
        course.setTeacher(teacher);
        course.addGroup(group);
    }

    private static Stream<Arguments> userRoles() {
        return Stream.of(Arguments.of("admin@gmail.com", "ADMIN"), Arguments.of("student@gmail.com", "STUDENT"),
                Arguments.of("teacher@gmail.com", "TEACHER"), Arguments.of("staff@gmail.com", "STAFF"));
    }

    @ParameterizedTest
    @MethodSource("userRoles")
    void getCourses_shouldReturnCorrectModelForUser(String username, String role) throws Exception {
        List<Course> courseList = List.of(course);

        Pageable pageable = PageRequest.of(0, 5);
        Page<Course> coursePage = new PageImpl<>(courseList, pageable, courseList.size());

        when(service.findAll(pageable)).thenReturn(coursePage);

        mockMvc.perform(get("/courses").param("page", "0").param("size", "5").with(user(username).roles(role)))
                .andExpect(status().isOk()).andExpect(view().name("view-all-courses"))
                .andExpect(model().attributeExists("courses")).andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages")).andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("totalPages", 1)).andExpect(model().attribute("courses", coursePage));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void showCreateCourseForm_shouldReturnModelWithNewCourse() throws Exception {
        mockMvc.perform(get("/courses/add")).andExpect(status().isOk()).andExpect(view().name("add-new-course"))
                .andExpect(model().attributeExists("course")).andExpect(model().attributeExists("teacherPage"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createCourse_shouldRedirectWithSuccess_whenCreatedValidCourse() throws Exception {
        mockMvc.perform(post("/courses/add").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Computer Science")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"))
                .andExpect(flash().attribute("successMessage", "Course added successfully!"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createCourse_shouldRedirectWithError_whenServiceThrowsException() throws Exception {
        Course newCourse = new Course();
        newCourse.setName("Computer Science");

        doThrow(new IllegalArgumentException("Service error")).when(service).addNew(newCourse);

        mockMvc.perform(post("/courses/add").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", newCourse.getName())).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"))
                .andExpect(flash().attribute("errorMessage", "Error: Service error"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createCourse_shouldReturnWithErrors_whenBindingExceptionOccurs() throws Exception {
        Course newCourse = new Course();
        newCourse.setName(" ");

        mockMvc.perform(post("/courses/add").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", newCourse.getName())).andExpect(status().isOk()).andExpect(view().name("add-new-course"))
                .andExpect(model().attributeHasFieldErrors("course", "name"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void showEditCourseForm_shouldReturnViewWithCourse_whenCourseExists() throws Exception {
        when(service.getByIdOrThrow(1L)).thenReturn(course);

        mockMvc.perform(get("/courses/edit/1")).andExpect(status().isOk()).andExpect(view().name("edit-course"))
                .andExpect(model().attribute("course", course));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void showEditCourseForm_shouldRedirectWithError_whenServiceFails() throws Exception {
        when(service.getByIdOrThrow(1L)).thenThrow(new CourseNotFoundException(1L));

        mockMvc.perform(get("/courses/edit/1")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"))
                .andExpect(flash().attribute("errorMessage", "Error: Course with ID: 1 not found"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateCourse_shouldRedirectWithSuccess_whenInputValidFields() throws Exception {

        Course modifiedCourse = new Course();
        modifiedCourse.setId(1L);
        modifiedCourse.setName("Computer Science 2");

        mockMvc.perform(post("/courses/edit/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", modifiedCourse.getName())).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"))
                .andExpect(flash().attribute("successMessage", "Course updated successfully!"));

        verify(service).update(modifiedCourse);
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateCourse_shouldRedirectWithError_whenUpdateFails() throws Exception {

        Course modifiedCourse = new Course();
        modifiedCourse.setId(1L);
        modifiedCourse.setName("Coumputer Science 2");

        when(service.update(modifiedCourse)).thenThrow(new IllegalArgumentException("Service Error"));

        mockMvc.perform(post("/courses/edit/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", modifiedCourse.getName())).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"))
                .andExpect(flash().attribute("errorMessage", "Error: Service Error"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateCourse_shouldReturnWithErrors_whenBindingExceptionOccurs() throws Exception {
        Course modifiedCourse = new Course();
        modifiedCourse.setName(" ");

        mockMvc.perform(post("/courses/edit/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", modifiedCourse.getName())).andExpect(status().isOk())
                .andExpect(view().name("edit-course")).andExpect(model().attributeHasFieldErrors("course", "name"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteCourse_shouldRedirectWithSuccess_whenCourseExistsInDb() throws Exception {
        when(service.getByIdOrThrow(1L)).thenReturn(course);

        mockMvc.perform(delete("/courses/delete/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/courses"))
                .andExpect(flash().attribute("successMessage", "Course deleted successfully!"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteCourse_shouldRedirectWithError_whenCourseDoesNotExistsInDb() throws Exception {
        doThrow(new CourseNotFoundException(1L)).when(service).deleteOrThrow(1L);

        mockMvc.perform(delete("/courses/delete/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/courses"))
                .andExpect(flash().attribute("errorMessage", "Error: Course with ID: 1 not found"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteCourse_shouldRedirectWithError_whenServiceFails() throws Exception {
        doThrow(new IllegalArgumentException("Service error")).when(service).deleteOrThrow(1L);

        mockMvc.perform(delete("/courses/delete/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/courses"))
                .andExpect(flash().attribute("errorMessage", "Error: Service error"));
    }

    @ParameterizedTest
    @MethodSource("userRoles")
    void getCoursesForUser_shouldReturnCoursesForUser_whenServiceReturnCourseList(String username, String role)
            throws Exception {
        Course course1 = new Course();
        course1.setId(1L);
        course1.setName("Math");
        Course course2 = new Course();
        course2.setId(2L);
        course2.setName("Physics");

        List<Course> courses = new ArrayList<>();
        courses.add(course1);
        courses.add(course2);

        when(service.findForUserWithUsername(username, role)).thenReturn(courses);

        mockMvc.perform(get("/courses/my-courses").with(user(username).roles(role))).andExpect(status().isOk())
                .andExpect(view().name("view-my-courses")).andExpect(model().attribute("courses", courses));
    }

    @ParameterizedTest
    @MethodSource("userRoles")
    void getCoursesForUser_shouldReturnEmptyCoursesForUser_whenServiceReturnEmptyList(String username, String role)
            throws Exception {
        List<Course> courses = new ArrayList<>();

        when(service.findForUserWithUsername(username, role)).thenReturn(courses);

        mockMvc.perform(get("/courses/my-courses").with(user(username).roles(role))).andExpect(status().isOk())
                .andExpect(view().name("view-my-courses")).andExpect(model().attribute("courses", courses));
    }

}
