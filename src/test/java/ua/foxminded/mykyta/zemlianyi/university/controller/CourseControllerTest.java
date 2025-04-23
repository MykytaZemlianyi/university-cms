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

    @Test
    void getCourses_shouldReturnCorrectModel() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setName("Marek");
        teacher.setSurname("Szepski");

        Group group = new Group();
        group.setName("AA-11");

        Course course = new Course();
        course.setId(1L);
        course.setName("Math");
        course.setTeacher(teacher);
        course.addGroup(group);

        List<Course> courseList = new ArrayList<>();
        courseList.add(course);

        Pageable pageable = PageRequest.of(0, 5);
        Page<Course> coursePage = new PageImpl<Course>(courseList, pageable, courseList.size());

        when(service.findAll(pageable)).thenReturn(coursePage);

        mockMvc.perform(get("/admin/courses").param("page", "0").param("size", "5")
                .with(user("admin@gmail.com").roles("ADMIN"))).andExpect(status().isOk())
                .andExpect(view().name("view-all-courses")).andExpect(model().attributeExists("courses"))
                .andExpect(model().attributeExists("currentPage")).andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attribute("currentPage", 0)).andExpect(model().attribute("totalPages", 1))
                .andExpect(model().attribute("courses", coursePage));
    }

}
