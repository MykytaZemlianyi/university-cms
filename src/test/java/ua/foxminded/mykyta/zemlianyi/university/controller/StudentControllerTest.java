package ua.foxminded.mykyta.zemlianyi.university.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import ua.foxminded.mykyta.zemlianyi.university.dto.Group;
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;
import ua.foxminded.mykyta.zemlianyi.university.service.StudentService;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    StudentService service;

    @Test
    void getStudents_shouldReturnCorrectModel() throws Exception {
        Group group = new Group();
        group.setName("A1");

        Student student = new Student();
        student.setId(1L);
        student.setName("Mykyta");
        student.setSurname("Zemlianyi");
        student.setEmail("mzeml@gmail.com");
        student.setGroup(group);

        List<Student> studentsList = new ArrayList<>();
        studentsList.add(student);

        Pageable pageable = PageRequest.of(0, 5);
        Page<Student> studentsPage = new PageImpl<Student>(studentsList, pageable, studentsList.size());

        when(service.findAll(pageable)).thenReturn(studentsPage);

        mockMvc.perform(get("/students").param("page", "0").param("size", "5")
                .with(user("zemlianoyne@gmail.com").roles("ADMIN"))).andExpect(status().isOk())
                .andExpect(view().name("/view-all-students")).andExpect(model().attributeExists("students"))
                .andExpect(model().attributeExists("currentPage")).andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attribute("currentPage", 0)).andExpect(model().attribute("totalPages", 1))
                .andExpect(model().attribute("students", studentsPage));
    }

}
