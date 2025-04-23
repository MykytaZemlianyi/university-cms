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

import ua.foxminded.mykyta.zemlianyi.university.dto.Course;
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;
import ua.foxminded.mykyta.zemlianyi.university.service.TeacherService;

@SpringBootTest
@AutoConfigureMockMvc
class TeacherControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TeacherService service;

    @Test
    void getTeachers_shouldReturnCorrectModel() throws Exception {
        Course course = new Course();
        course.setName("IT");
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("Mykyta");
        teacher.setSurname("Zemlianyi");
        teacher.setEmail("mzeml@gmail.com");
        teacher.addCourse(course);

        List<Teacher> teachersList = new ArrayList<>();
        teachersList.add(teacher);

        Pageable pageable = PageRequest.of(0, 5);
        Page<Teacher> teachersPage = new PageImpl<Teacher>(teachersList, pageable, teachersList.size());

        when(service.findAll(pageable)).thenReturn(teachersPage);

        mockMvc.perform(get("/admin/teachers").param("page", "0").param("size", "5")
                .with(user("admin@gmail.com").roles("ADMIN"))).andExpect(status().isOk())
                .andExpect(view().name("view-all-teachers")).andExpect(model().attributeExists("teachers"))
                .andExpect(model().attributeExists("currentPage")).andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attribute("currentPage", 0)).andExpect(model().attribute("totalPages", 1))
                .andExpect(model().attribute("teachers", teachersPage));
    }

}
