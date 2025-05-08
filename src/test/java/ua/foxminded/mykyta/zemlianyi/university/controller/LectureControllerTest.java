package ua.foxminded.mykyta.zemlianyi.university.controller;

import static org.mockito.Mockito.doReturn;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import ua.foxminded.mykyta.zemlianyi.university.dto.Lecture;
import ua.foxminded.mykyta.zemlianyi.university.dto.LectureForm;
import ua.foxminded.mykyta.zemlianyi.university.dto.LectureType;
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

    Lecture lecture = new Lecture();
    LectureForm form = new LectureForm();

    @BeforeEach
    void setUp() {
        Room room = new Room();
        room.setNumber(102);

        Teacher teacher = new Teacher();
        teacher.setName("Marek");
        teacher.setSurname("Szepski");

        Group group = new Group();
        group.setId(1L);
        group.setName("AA-11");

        Course course = new Course();
        course.setId(1L);
        course.setName("Math");
        course.setTeacher(teacher);
        course.addGroup(group);

        lecture.setId(1L);
        lecture.setLectureType(LectureType.SEMINAR);
        lecture.setCourse(course);
        lecture.setRoom(room);
        lecture.setTimeStart(LocalDateTime.now());
        lecture.setTimeEnd(LocalDateTime.now());

        form.setId(1L);
        form.setLectureType(LectureType.SEMINAR);
        form.setCourseId(1L);
        form.setRoomId(1L);
        form.setDate(LocalDate.now());
        form.setTimeStart(LocalTime.now());
        form.setTimeEnd(LocalTime.now());
    }

    @Test
    void getLectures_shouldReturnCorrectModel() throws Exception {

        List<Lecture> lectureList = new ArrayList<>();
        lectureList.add(lecture);

        Pageable pageable = PageRequest.of(0, 5);
        Page<Lecture> lecturePage = new PageImpl<Lecture>(lectureList, pageable, lectureList.size());

        when(service.findAll(pageable)).thenReturn(lecturePage);

        mockMvc.perform(get("/admin/lectures").param("page", "0").param("size", "5")
                .with(user("admin@gmail.com").roles("ADMIN"))).andExpect(status().isOk())
                .andExpect(view().name("view-all-lectures")).andExpect(model().attributeExists("lectures"))
                .andExpect(model().attributeExists("currentPage")).andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attribute("currentPage", 0)).andExpect(model().attribute("totalPages", 1))
                .andExpect(model().attribute("lectures", lecturePage));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void showCreateLectureForm_shouldReturnModelWithNewLecture() throws Exception {
        mockMvc.perform(get("/admin/add-lecture")).andExpect(status().isOk()).andExpect(view().name("add-new-lecture"))
                .andExpect(model().attributeExists("lectureForm")).andExpect(model().attributeExists("courseList"))
                .andExpect(model().attributeExists("roomList")).andExpect(model().attributeExists("lectureTypes"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createLecture_shouldRedirectWithSuccess_whenCreatedValidLecture() throws Exception {
        mockMvc.perform(post("/admin/add-lecture").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("lectureType", "SEMINAR").param("courseId", "1").param("roomId", "1")
                .param("date", LocalDate.now().toString()).param("timeStart", "10:00").param("timeEnd", "11:00"))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin/lectures"))
                .andExpect(flash().attribute("successMessage", "Lecture added successfully!"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createLecture_shouldRedirectWithError_whenServiceThrowsException() throws Exception {
        doReturn(lecture).when(service).mapFormToLecture(Mockito.any(LectureForm.class));
        doThrow(new IllegalArgumentException("Service error")).when(service).addNew(Mockito.any(Lecture.class));

        mockMvc.perform(post("/admin/add-lecture").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("lectureType", "SEMINAR").param("courseId", "1").param("groupId", "1").param("roomId", "1")
                .param("date", LocalDate.now().toString()).param("timeStart", "10:00").param("timeEnd", "11:00"))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin/lectures"))
                .andExpect(flash().attribute("errorMessage", "Error: Service error"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createLecture_shouldReturnWithErrors_whenBindingExceptionOccurs() throws Exception {

        mockMvc.perform(post("/admin/add-lecture").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("lectureType", "SEMINAR").param("courseId", "").param("groupId", "").param("roomId", "")
                .param("date", LocalDate.now().toString()).param("timeStart", LocalTime.now().toString())
                .param("timeEnd", LocalTime.now().toString())).andExpect(status().isOk())
                .andExpect(view().name("add-new-lecture"))
                .andExpect(model().attributeHasFieldErrors("lectureForm", "courseId"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void showEditLectureForm_shouldReturnViewWithLecture_whenLectureExists() throws Exception {
        Optional<Lecture> lectureOpt = Optional.of(lecture);
        when(service.findById(1L)).thenReturn(lectureOpt);
        when(service.mapLectureToForm(lecture)).thenReturn(form);

        mockMvc.perform(get("/admin/edit-lecture/1")).andExpect(status().isOk()).andExpect(view().name("edit-lecture"))
                .andExpect(model().attribute("lectureForm", form));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void showEditLectureForm_shouldRedirectWithError_whenServiceFails() throws Exception {
        Optional<Lecture> emptyLectureOpt = Optional.empty();
        when(service.findById(1L)).thenReturn(emptyLectureOpt);

        mockMvc.perform(get("/admin/edit-lecture/1")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/lectures"))
                .andExpect(flash().attribute("errorMessage", "Error: " + Constants.OBJECT_UPDATE_FAIL_DOES_NOT_EXIST));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateLecture_shouldRedirectWithSuccess_whenInputValidFields() throws Exception {

        mockMvc.perform(post("/admin/edit-lecture/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("lectureType", "SEMINAR").param("courseId", "2").param("groupId", "2").param("roomId", "2")
                .param("date", LocalDate.now().toString()).param("timeStart", LocalTime.now().toString())
                .param("timeEnd", LocalTime.now().toString())).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/lectures"))
                .andExpect(flash().attribute("successMessage", "Lecture updated successfully!"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateLecture_shouldRedirectWithError_whenUpdateFails() throws Exception {

        when(service.mapFormToLecture(Mockito.any(LectureForm.class))).thenReturn(lecture);
        when(service.update(Mockito.any(Lecture.class))).thenThrow(new IllegalArgumentException("Service Error"));

        mockMvc.perform(post("/admin/edit-lecture/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("lectureType", "SEMINAR").param("courseId", "2").param("groupId", "2").param("roomId", "2")
                .param("date", LocalDate.now().toString()).param("timeStart", LocalTime.now().toString())
                .param("timeEnd", LocalTime.now().toString())).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/lectures"))
                .andExpect(flash().attribute("errorMessage", "Error: Service Error"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateLecture_shouldReturnWithErrors_whenBindingExceptionOccurs() throws Exception {

        mockMvc.perform(post("/admin/edit-lecture/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("lectureType", "SEMINAR").param("courseId", "").param("groupId", "").param("roomId", "")
                .param("date", LocalDate.now().toString()).param("timeStart", LocalTime.now().toString())
                .param("timeEnd", LocalTime.now().toString())).andExpect(status().isOk())
                .andExpect(view().name("edit-lecture"))
                .andExpect(model().attributeHasFieldErrors("lectureForm", "courseId"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteLecture_shouldRedirectWithSuccess_whenLectureExistsInDb() throws Exception {
        when(service.findById(1L)).thenReturn(Optional.of(lecture));

        mockMvc.perform(
                delete("/admin/delete-lecture/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin/lectures"))
                .andExpect(flash().attribute("successMessage", "Lecture deleted successfully!"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteLecture_shouldRedirectWithError_whenLectureDoesNotExistsInDb() throws Exception {
        when(service.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(
                delete("/admin/delete-lecture/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin/lectures"))
                .andExpect(flash().attribute("errorMessage", "Error: Lecture does not exists"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteLecture_shouldRedirectWithError_whenServiceFails() throws Exception {
        when(service.findById(1L)).thenThrow(new IllegalArgumentException("Service error"));

        mockMvc.perform(
                delete("/admin/delete-lecture/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin/lectures"))
                .andExpect(flash().attribute("errorMessage", "Error: Service error"));
    }
}
