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
import java.util.Optional;

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

import ua.foxminded.mykyta.zemlianyi.university.Constants;
import ua.foxminded.mykyta.zemlianyi.university.dto.Course;
import ua.foxminded.mykyta.zemlianyi.university.dto.Group;
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;
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

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void getCourses_shouldReturnCorrectModel() throws Exception {

        List<Course> courseList = new ArrayList<>();
        courseList.add(course);

        Pageable pageable = PageRequest.of(0, 5);
        Page<Course> coursePage = new PageImpl<Course>(courseList, pageable, courseList.size());

        when(service.findAll(pageable)).thenReturn(coursePage);

        mockMvc.perform(get("/admin/courses").param("page", "0").param("size", "5")).andExpect(status().isOk())
                .andExpect(view().name("view-all-courses")).andExpect(model().attributeExists("courses"))
                .andExpect(model().attributeExists("currentPage")).andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attribute("currentPage", 0)).andExpect(model().attribute("totalPages", 1))
                .andExpect(model().attribute("courses", coursePage));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void showCreateCourseForm_shouldReturnModelWithNewCourse() throws Exception {
        mockMvc.perform(get("/admin/add-new-course")).andExpect(status().isOk())
                .andExpect(view().name("add-new-course")).andExpect(model().attributeExists("course"))
                .andExpect(model().attributeExists("teacherList"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createCourse_shouldRedirectWithSuccess_whenCreatedValidCourse() throws Exception {
        mockMvc.perform(post("/admin/add-course").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Computer Science")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/courses"))
                .andExpect(flash().attribute("successMessage", "Course added successfully!"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createCourse_shouldRedirectWithError_whenServiceThrowsException() throws Exception {
        Course newCourse = new Course();
        newCourse.setName("Computer Science");

        doThrow(new IllegalArgumentException("Service error")).when(service).addNew(newCourse);

        mockMvc.perform(post("/admin/add-course").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", newCourse.getName())).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/courses"))
                .andExpect(flash().attribute("errorMessage", "Error: Service error"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createCourse_shouldReturnWithErrors_whenBindingExceptionOccurs() throws Exception {
        Course newCourse = new Course();
        newCourse.setName(" ");

        mockMvc.perform(post("/admin/add-course").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", newCourse.getName())).andExpect(status().isOk()).andExpect(view().name("add-new-course"))
                .andExpect(model().attributeHasFieldErrors("course", "name"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void showEditCourseForm_shouldReturnViewWithCourse_whenCourseExists() throws Exception {
        Optional<Course> courseOpt = Optional.of(course);
        when(service.findById(1L)).thenReturn(courseOpt);

        mockMvc.perform(get("/admin/edit-course/1")).andExpect(status().isOk()).andExpect(view().name("edit-course"))
                .andExpect(model().attribute("course", course));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void showEditCourseForm_shouldRedirectWithError_whenServiceFails() throws Exception {
        Optional<Course> emptyCourseOpt = Optional.empty();
        when(service.findById(1L)).thenReturn(emptyCourseOpt);

        mockMvc.perform(get("/admin/edit-course/1")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/courses"))
                .andExpect(flash().attribute("errorMessage", "Error: " + Constants.OBJECT_UPDATE_FAIL_DOES_NOT_EXIST));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateCourse_shouldRedirectWithSuccess_whenInputValidFields() throws Exception {

        Course modifiedCourse = new Course();
        modifiedCourse.setId(1L);
        modifiedCourse.setName("Computer Science 2");

        mockMvc.perform(post("/admin/edit-course/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", modifiedCourse.getName())).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/courses"))
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

        mockMvc.perform(post("/admin/edit-course/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", modifiedCourse.getName())).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/courses"))
                .andExpect(flash().attribute("errorMessage", "Error: Service Error"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateCourse_shouldReturnWithErrors_whenBindingExceptionOccurs() throws Exception {
        Course modifiedCourse = new Course();
        modifiedCourse.setName(" ");

        mockMvc.perform(post("/admin/edit-course/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", modifiedCourse.getName())).andExpect(status().isOk())
                .andExpect(view().name("edit-course")).andExpect(model().attributeHasFieldErrors("course", "name"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteCourse_shouldRedirectWithSuccess_whenCourseExistsInDb() throws Exception {
        when(service.findById(1L)).thenReturn(Optional.of(course));

        mockMvc.perform(
                delete("/admin/delete-course/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin/courses"))
                .andExpect(flash().attribute("successMessage", "Course deleted successfully!"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteCourse_shouldRedirectWithError_whenCourseDoesNotExistsInDb() throws Exception {
        when(service.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(
                delete("/admin/delete-course/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin/courses"))
                .andExpect(flash().attribute("errorMessage", "Error: Course does not exists"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteCourse_shouldRedirectWithError_whenServiceFails() throws Exception {
        when(service.findById(1L)).thenThrow(new IllegalArgumentException("Service error"));

        mockMvc.perform(
                delete("/admin/delete-course/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin/courses"))
                .andExpect(flash().attribute("errorMessage", "Error: Service error"));
    }

}
