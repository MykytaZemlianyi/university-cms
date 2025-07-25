package ua.foxminded.mykyta.zemlianyi.university.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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

import ua.foxminded.mykyta.zemlianyi.university.dto.Course;
import ua.foxminded.mykyta.zemlianyi.university.dto.DatePicker;
import ua.foxminded.mykyta.zemlianyi.university.dto.Group;
import ua.foxminded.mykyta.zemlianyi.university.dto.Lecture;
import ua.foxminded.mykyta.zemlianyi.university.dto.LectureForm;
import ua.foxminded.mykyta.zemlianyi.university.dto.LectureType;
import ua.foxminded.mykyta.zemlianyi.university.dto.Room;
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;
import ua.foxminded.mykyta.zemlianyi.university.exceptions.LectureNotFoundException;
import ua.foxminded.mykyta.zemlianyi.university.service.LectureService;
import ua.foxminded.mykyta.zemlianyi.university.service.TeacherService;

@SpringBootTest
@AutoConfigureMockMvc
class LectureControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    LectureService service;

    @MockitoBean
    TeacherService teacherService;

    Teacher teacher = new Teacher();
    Lecture lecture = new Lecture();
    LectureForm form = new LectureForm();

    @BeforeEach
    void setUp() {
        Room room = new Room();
        room.setNumber(102);

        teacher.setId(1L);
        teacher.setName("Marek");
        teacher.setSurname("Szepski");
        teacher.setEmail("mszepski@gmail.com");

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
        lecture.setTimeStart(LocalDateTime.now());
        lecture.setTimeEnd(LocalDateTime.now().plusHours(1));
        lecture.setRoom(room);

        form.setId(1L);
        form.setLectureType(LectureType.SEMINAR);
        form.setCourseId(1L);
        form.setRoomId(1L);
        form.setDate(LocalDate.now());
        form.setTimeStart(LocalTime.now());
        form.setTimeEnd(LocalTime.now().plusHours(1));
    }

    @ParameterizedTest
    @MethodSource("userRoles")
    void getLectures_shouldReturnCorrectModel(String username, String role) throws Exception {

        List<Lecture> lectureList = new ArrayList<>();
        lectureList.add(lecture);

        Pageable pageable = PageRequest.of(0, 5);
        Page<Lecture> lecturePage = new PageImpl<Lecture>(lectureList, pageable, lectureList.size());

        when(service.findAll(pageable)).thenReturn(lecturePage);

        mockMvc.perform(get("/lectures").param("page", "0").param("size", "5").with(user(username).roles(role)))
                .andExpect(status().isOk()).andExpect(view().name("view-all-lectures"))
                .andExpect(model().attributeExists("lectures")).andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages")).andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("totalPages", 1)).andExpect(model().attribute("lectures", lecturePage));
    }

    private static Stream<Arguments> userRoles() {
        return Stream.of(Arguments.of("admin@gmail.com", "ADMIN"), Arguments.of("student@gmail.com", "STUDENT"),
                Arguments.of("teacher@gmail.com", "TEACHER"), Arguments.of("staff@gmail.com", "STAFF"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void showCreateLectureForm_shouldReturnModelWithNewLecture() throws Exception {
        mockMvc.perform(get("/lectures/add")).andExpect(status().isOk()).andExpect(view().name("add-new-lecture"))
                .andExpect(model().attributeExists("lectureForm")).andExpect(model().attributeExists("coursePage"))
                .andExpect(model().attributeExists("roomPage")).andExpect(model().attributeExists("lectureTypes"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createLecture_shouldRedirectWithSuccess_whenCreatedValidLecture() throws Exception {
        mockMvc.perform(post("/lectures/add").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("lectureType", "SEMINAR").param("courseId", "1").param("roomId", "1")
                .param("date", LocalDate.now().toString()).param("timeStart", "10:00").param("timeEnd", "11:00"))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/lectures"))
                .andExpect(flash().attribute("successMessage", "Lecture added successfully!"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createLecture_shouldRedirectWithError_whenServiceThrowsException() throws Exception {
        doThrow(new IllegalArgumentException("Service error")).when(service)
                .addNewFromForm(Mockito.any(LectureForm.class));

        mockMvc.perform(post("/lectures/add").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("lectureType", "SEMINAR").param("courseId", "1").param("groupId", "1").param("roomId", "1")
                .param("date", LocalDate.now().toString()).param("timeStart", "10:00").param("timeEnd", "11:00"))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/lectures"))
                .andExpect(flash().attribute("errorMessage", "Error: Service error"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void createLecture_shouldReturnWithErrors_whenBindingExceptionOccurs() throws Exception {

        mockMvc.perform(post("/lectures/add").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("lectureType", "SEMINAR").param("courseId", "").param("groupId", "").param("roomId", "")
                .param("date", LocalDate.now().toString()).param("timeStart", LocalTime.now().toString())
                .param("timeEnd", LocalTime.now().toString())).andExpect(status().isOk())
                .andExpect(view().name("add-new-lecture"))
                .andExpect(model().attributeHasFieldErrors("lectureForm", "courseId"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void showEditLectureForm_shouldReturnViewWithLecture_whenLectureExists() throws Exception {
        when(service.getByIdOrThrow(1L)).thenReturn(lecture);
        when(service.mapLectureToForm(lecture)).thenReturn(form);

        mockMvc.perform(get("/lectures/edit/1")).andExpect(status().isOk()).andExpect(view().name("edit-lecture"))
                .andExpect(model().attribute("lectureForm", form));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void showEditLectureForm_shouldRedirectWithError_whenServiceFails() throws Exception {
        when(service.getByIdOrThrow(1L)).thenThrow(new LectureNotFoundException(1L));

        mockMvc.perform(get("/lectures/edit/1")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lectures"))
                .andExpect(flash().attribute("errorMessage", "Error: Lecture with ID: 1 not found"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateLecture_shouldRedirectWithSuccess_whenInputValidFields() throws Exception {

        mockMvc.perform(post("/lectures/edit/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("lectureType", "SEMINAR").param("courseId", "2").param("groupId", "2").param("roomId", "2")
                .param("date", LocalDate.now().toString()).param("timeStart", LocalTime.now().toString())
                .param("timeEnd", LocalTime.now().toString())).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lectures"))
                .andExpect(flash().attribute("successMessage", "Lecture updated successfully!"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateLecture_shouldRedirectWithError_whenUpdateFails() throws Exception {

        when(service.updateFromForm(Mockito.any(LectureForm.class)))
                .thenThrow(new IllegalArgumentException("Service Error"));

        mockMvc.perform(post("/lectures/edit/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("lectureType", "SEMINAR").param("courseId", "2").param("groupId", "2").param("roomId", "2")
                .param("date", LocalDate.now().toString()).param("timeStart", LocalTime.now().toString())
                .param("timeEnd", LocalTime.now().toString())).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lectures"))
                .andExpect(flash().attribute("errorMessage", "Error: Service Error"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void updateLecture_shouldReturnWithErrors_whenBindingExceptionOccurs() throws Exception {

        mockMvc.perform(post("/lectures/edit/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("lectureType", "").param("courseId", "").param("groupId", "").param("roomId", "")
                .param("date", LocalDate.now().toString()).param("timeStart", LocalTime.now().toString())
                .param("timeEnd", LocalTime.now().toString())).andExpect(status().isOk())
                .andExpect(view().name("edit-lecture"))
                .andExpect(model().attributeHasFieldErrors("lectureForm", "courseId"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteLecture_shouldRedirectWithSuccess_whenLectureExistsInDb() throws Exception {
        when(service.findById(1L)).thenReturn(Optional.of(lecture));

        mockMvc.perform(delete("/lectures/delete/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/lectures"))
                .andExpect(flash().attribute("successMessage", "Lecture deleted successfully!"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteLecture_shouldRedirectWithError_whenLectureDoesNotExistsInDb() throws Exception {
        doThrow(new LectureNotFoundException(1L)).when(service).deleteByIdOrThrow(1L);

        mockMvc.perform(delete("/lectures/delete/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/lectures"))
                .andExpect(flash().attribute("errorMessage", "Error: Lecture with ID: 1 not found"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void deleteLecture_shouldRedirectWithError_whenServiceFails() throws Exception {
        doThrow(new IllegalArgumentException("Service error")).when(service).deleteByIdOrThrow(1L);

        mockMvc.perform(delete("/lectures/delete/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/lectures"))
                .andExpect(flash().attribute("errorMessage", "Error: Service error"));
    }

    @ParameterizedTest
    @MethodSource("userRolesValidForGetMyScheduleRequest")
    void getMySchedule_ShouldReturnViewWithModelAttributes_whenUserValidForOperation(String user, String role)
            throws Exception {
        Page<Lecture> mockPage = new PageImpl<>(List.of(lecture));
        when(service.findForUserByEmailInTimeInterval(eq(user), eq(role), any(DatePicker.class), any(Pageable.class)))
                .thenReturn(mockPage);

        mockMvc.perform(get("/lectures/my-schedule").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(user(user).roles(role)).param("currentPage", "0").param("size", "5").param("preset", "TODAY")
                .param("startDate", "2024-06-01").param("endDate", "2024-06-30")).andExpect(status().isOk())
                .andExpect(view().name("view-my-schedule")).andExpect(model().attributeExists("datePicker"))
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("totalPages", mockPage.getTotalPages()))
                .andExpect(model().attribute("lectures", mockPage));

    }

    private static Stream<Arguments> userRolesValidForGetMyScheduleRequest() {
        return Stream.of(Arguments.of("student@gmail.com", "STUDENT"), Arguments.of("teacher@gmail.com", "TEACHER"));
    }

    @ParameterizedTest
    @MethodSource("userRolesInvalidForGetMyScheduleRequest")
    void getMySchedule_ShouldReturnAccessDenied_whenUserIsInvalidForOperation(String user, String role)
            throws Exception {

        mockMvc.perform(get("/lectures/my-schedule").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(user(user).roles(role))).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/welcome"))
                .andExpect(flash().attribute("errorMessage", "Error: Access Denied"));

    }

    private static Stream<Arguments> userRolesInvalidForGetMyScheduleRequest() {
        return Stream.of(Arguments.of("admin@gmail.com", "ADMIN"), Arguments.of("staff@gmail.com", "STAFF"));
    }

    @ParameterizedTest
    @MethodSource("userRoles")
    void getTeacherSchedule_ShouldReturnViewWithModel(String user, String role) throws Exception {

        when(teacherService.getByIdOrThrow(1L)).thenReturn(teacher);

        Page<Lecture> page = new PageImpl<>(List.of(lecture), PageRequest.of(0, 5), 1);
        when(service.findForUserByEmailInTimeInterval(eq(teacher.getEmail()), eq("TEACHER"), any(DatePicker.class),
                any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/lectures/teacher-schedule/{id}", 1L).with(user(user).roles(role))
                .param("currentPage", "0").param("size", "5").param("preset", "TODAY")).andExpect(status().isOk())
                .andExpect(view().name("view-teacher-schedule")).andExpect(model().attributeExists("datePicker"))
                .andExpect(model().attribute("teacherFullName", "Marek Szepski"))
                .andExpect(model().attribute("lectures", page)).andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("totalPages", 1));
    }
}
