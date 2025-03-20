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
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;
import ua.foxminded.mykyta.zemlianyi.university.service.GroupService;

@SpringBootTest
@AutoConfigureMockMvc
class GroupControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    GroupService service;

    @Test
    void getGroups_shouldReturnCorrectModel() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setName("Marek");
        teacher.setSurname("Szepski");

        Course course = new Course();
        course.setTeacher(teacher);

        Student student = new Student();

        Group group = new Group();
        group.setId(1L);
        group.setName("AA-11");
        group.addCourse(course);
        group.addStudent(student);

        List<Group> groupList = new ArrayList<>();
        groupList.add(group);

        Pageable pageable = PageRequest.of(0, 5);
        Page<Group> groupPage = new PageImpl<Group>(groupList, pageable, groupList.size());

        when(service.findAll(pageable)).thenReturn(groupPage);

        mockMvc.perform(
                get("/groups").param("page", "0").param("size", "5").with(user("zemlianoyne@gmail.com").roles("ADMIN")))
                .andExpect(status().isOk()).andExpect(view().name("tables/groups"))
                .andExpect(model().attributeExists("groups")).andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages")).andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("totalPages", 1)).andExpect(model().attribute("groups", groupPage));
    }

}
