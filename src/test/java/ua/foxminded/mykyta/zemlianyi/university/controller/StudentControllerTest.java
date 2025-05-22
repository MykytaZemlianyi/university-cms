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

import ua.foxminded.mykyta.zemlianyi.university.dto.Group;
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;
import ua.foxminded.mykyta.zemlianyi.university.exceptions.StudentNotFoundException;
import ua.foxminded.mykyta.zemlianyi.university.service.StudentService;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    StudentService service;

    Student student = new Student();

    @BeforeEach
    void setUp() {
        student.setId(1L);
        student.setName("Mykyta");
        student.setSurname("Zemlianyi");
        student.setEmail("mzeml@gmail.com");

        Group group = new Group();
        group.setName("A1");
        student.setGroup(group);
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void getStudents_shouldReturnCorrectModel() throws Exception {

        List<Student> studentsList = new ArrayList<>();
        studentsList.add(student);

        Pageable pageable = PageRequest.of(0, 5);
        Page<Student> studentsPage = new PageImpl<Student>(studentsList, pageable, studentsList.size());

        when(service.findAll(pageable)).thenReturn(studentsPage);

        mockMvc.perform(get("/students").param("page", "0").param("size", "5")).andExpect(status().isOk())
                .andExpect(view().name("view-all-students")).andExpect(model().attributeExists("students"))
                .andExpect(model().attributeExists("currentPage")).andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attribute("currentPage", 0)).andExpect(model().attribute("totalPages", 1))
                .andExpect(model().attribute("students", studentsPage));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void showCreateStudentForm_shouldReturnModelWithNewStudent() throws Exception {
        mockMvc.perform(get("/students/add")).andExpect(status().isOk()).andExpect(view().name("add-new-student"))
                .andExpect(model().attributeExists("student"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createStudent_shouldRedirectWithSuccess_whenCreatedValidStudent() throws Exception {
        mockMvc.perform(post("/students/add").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Marek").param("surname", "Szepski").param("email", "mszepski@gmail.com")
                .param("password", "12345").param("group", "1")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"))
                .andExpect(flash().attribute("successMessage", "Student added successfully!"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createStudent_shouldRedirectWithError_whenServiceThrowsException() throws Exception {
        Student newStudent = new Student();
        newStudent.setName("Mykyta");
        newStudent.setSurname("Zemlianyi");
        newStudent.setEmail("mzeml@gmail.com");
        newStudent.setPassword("12345");

        doThrow(new IllegalArgumentException("Service error")).when(service).addNew(newStudent);

        mockMvc.perform(post("/students/add").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", newStudent.getName()).param("surname", newStudent.getSurname())
                .param("email", newStudent.getEmail()).param("password", newStudent.getPassword()))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/students"))
                .andExpect(flash().attribute("errorMessage", "Error: Service error"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createStudent_shouldReturnWithErrors_whenBindingExceptionOccurs() throws Exception {
        Student newStudent = new Student();
        newStudent.setName(" ");
        newStudent.setSurname("Zemlianyi");
        newStudent.setEmail("mzeml@gmail.com");
        newStudent.setPassword("12345");

        mockMvc.perform(post("/students/add").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", newStudent.getName()).param("surname", newStudent.getSurname())
                .param("email", newStudent.getEmail()).param("password", newStudent.getPassword()))
                .andExpect(status().isOk()).andExpect(view().name("add-new-student"))
                .andExpect(model().attributeHasFieldErrors("student", "name"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void showEditStudentForm_shouldReturnViewWithStudent_whenStudentExists() throws Exception {
        when(service.getByIdOrThrow(1L)).thenReturn(student);

        mockMvc.perform(get("/students/edit/1")).andExpect(status().isOk()).andExpect(view().name("edit-student"))
                .andExpect(model().attribute("student", student));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void showEditStudentForm_shouldRedirectWithError_whenServiceFails() throws Exception {
        when(service.getByIdOrThrow(1L)).thenThrow(new StudentNotFoundException(1L));

        mockMvc.perform(get("/students/edit/1")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"))
                .andExpect(flash().attribute("errorMessage", "Error: Student with ID: 1 not found"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateStudent_shouldRedirectWithSuccess_whenInputValidFields() throws Exception {

        Student modifiedStudent = new Student();
        modifiedStudent.setId(1L);
        modifiedStudent.setName("Marek");
        modifiedStudent.setSurname("Szepski");
        modifiedStudent.setEmail("mszepski@gmail.com");
        modifiedStudent.setPassword("12345");

        mockMvc.perform(post("/students/edit/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", modifiedStudent.getName()).param("surname", modifiedStudent.getSurname())
                .param("email", modifiedStudent.getEmail()).param("password", modifiedStudent.getPassword()))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/students"))
                .andExpect(flash().attribute("successMessage", "Student updated successfully!"));

        verify(service).update(modifiedStudent);
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateStudent_shouldRedirectWithError_whenUpdateFails() throws Exception {

        Student modifiedStudent = new Student();
        modifiedStudent.setId(1L);
        modifiedStudent.setName("Marek");
        modifiedStudent.setSurname("Szepski");
        modifiedStudent.setEmail("mszepski@gmail.com");
        modifiedStudent.setPassword("12345");

        when(service.update(modifiedStudent)).thenThrow(new IllegalArgumentException("Service Error"));

        mockMvc.perform(post("/students/edit/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", modifiedStudent.getName()).param("surname", modifiedStudent.getSurname())
                .param("email", modifiedStudent.getEmail()).param("password", modifiedStudent.getPassword()))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/students"))
                .andExpect(flash().attribute("errorMessage", "Error: Service Error"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateStudent_shouldReturnWithErrors_whenBindingExceptionOccurs() throws Exception {
        Student modifiedStudent = new Student();
        modifiedStudent.setName(" ");
        modifiedStudent.setSurname("Zemlianyi");
        modifiedStudent.setEmail("mzeml@gmail.com");
        modifiedStudent.setPassword("12345");

        mockMvc.perform(post("/students/edit/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", modifiedStudent.getName()).param("surname", modifiedStudent.getSurname())
                .param("email", modifiedStudent.getEmail()).param("password", modifiedStudent.getPassword()))
                .andExpect(status().isOk()).andExpect(view().name("edit-student"))
                .andExpect(model().attributeHasFieldErrors("student", "name"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteStudent_shouldRedirectWithSuccess_whenStudentExistsInDb() throws Exception {
        when(service.getByIdOrThrow(1L)).thenReturn(student);

        mockMvc.perform(delete("/students/delete/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/students"))
                .andExpect(flash().attribute("successMessage", "Student deleted successfully!"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteStudent_shouldRedirectWithError_whenStudentDoesNotExistsInDb() throws Exception {
        when(service.getByIdOrThrow(1L)).thenThrow(new StudentNotFoundException(1L));

        mockMvc.perform(delete("/students/delete/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/students"))
                .andExpect(flash().attribute("errorMessage", "Error: Student with ID: 1 not found"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteStudent_shouldRedirectWithError_whenServiceFails() throws Exception {
        when(service.getByIdOrThrow(1L)).thenThrow(new IllegalArgumentException("Service error"));

        mockMvc.perform(delete("/students/delete/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/students"))
                .andExpect(flash().attribute("errorMessage", "Error: Service error"));
    }

}
