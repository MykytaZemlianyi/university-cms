package ua.foxminded.mykyta.zemlianyi.university.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;
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
import ua.foxminded.mykyta.zemlianyi.university.dto.Lecture;
import ua.foxminded.mykyta.zemlianyi.university.dto.Room;
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;
import ua.foxminded.mykyta.zemlianyi.university.service.LectureService;

@SpringBootTest
@AutoConfigureMockMvc
class LectureControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    LectureService service;

    @Test
    void getLectures_shouldReturnCorrectModel() throws Exception {
        Room room = new Room();
        room.setNumber(102);

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

        Lecture lecture = new Lecture();
        lecture.setId(1L);
        lecture.setCourse(course);
        lecture.setRoom(room);
        lecture.setTimeStart(LocalDateTime.now());
        lecture.setTimeEnd(LocalDateTime.now());

        List<Lecture> lectureList = new ArrayList<>();
        lectureList.add(lecture);

        Pageable pageable = PageRequest.of(0, 5);
        Page<Lecture> lecturePage = new PageImpl<Lecture>(lectureList, pageable, lectureList.size());

        when(service.findAll(pageable)).thenReturn(lecturePage);

        mockMvc.perform(get("/lectures").param("page", "0").param("size", "5")
                .with(user("zemlianoyne@gmail.com").roles("ADMIN"))).andExpect(status().isOk())
                .andExpect(view().name("/view-all-lectures")).andExpect(model().attributeExists("lectures"))
                .andExpect(model().attributeExists("currentPage")).andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attribute("currentPage", 0)).andExpect(model().attribute("totalPages", 1))
                .andExpect(model().attribute("lectures", lecturePage));
    }

}
