package ua.foxminded.mykyta.zemlianyi.university.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import ua.foxminded.mykyta.zemlianyi.university.dao.LectureDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Course;
import ua.foxminded.mykyta.zemlianyi.university.dto.Lecture;
import ua.foxminded.mykyta.zemlianyi.university.dto.LectureType;

@SpringBootTest(classes = { LectureServiceImpl.class })
class LectureServiceImplTest {
    @MockitoBean
    LectureDao lectureDao;

    @Autowired
    LectureService lectureService;

    Lecture lecture = new Lecture();
    Course course = new Course();

    @BeforeEach
    void setUp() {
        course.setId(1L);
        course.setName("Good course");

        lecture.setId(1L);
        lecture.setLectureType(LectureType.LABORATORIUM);
        lecture.setCourse(course);
        lecture.setTimeStart(LocalDateTime.of(2025, 1, 16, 0, 0));
        lecture.setTimeEnd(LocalDateTime.of(2025, 1, 16, 23, 59, 59));
    }

    @Test
    void addNew_shouldThrowIllegarArgumentException_whenLectureNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            lectureService.addNew(null);
        });
    }

    @Test
    void addNew_shouldThrowIllegarArgumentException_whenLectureIsInvalid() {
        Lecture invalidLecture = new Lecture();
        assertThrows(IllegalArgumentException.class, () -> {
            lectureService.addNew(invalidLecture);
        });
    }

    @Test
    void addNew_shouldAddLecture_whenLectureIsValid() {
        lectureService.addNew(lecture);
        verify(lectureDao).save(lecture);
    }

    @Test
    void update_shouldThrowIllegalArgumentException_whenLectureNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            lectureService.update(null);
        });
    }

    @Test
    void update_shouldThrowIllegarArgumentException_whenLectureIsInvalid() {
        Lecture invalidLecture = new Lecture();
        assertThrows(IllegalArgumentException.class, () -> {
            lectureService.update(invalidLecture);
        });
    }

    @Test
    void update_shouldThrowIllegalArgumentException_whenLectureIsNotSavedInDb() {
        when(lectureDao.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            lectureService.update(lecture);
        });
    }

    @Test
    void update_shouldUpdateLecture_whenLectureIsValidAndSaved() {
        when(lectureDao.existsById(1L)).thenReturn(true);
        lectureService.update(lecture);
        verify(lectureDao).save(lecture);
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_when_lectureIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            lectureService.delete(null);
        });
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_when_lectureIsInvalid() {
        Lecture invalidLecture = new Lecture();
        assertThrows(IllegalArgumentException.class, () -> {
            lectureService.delete(invalidLecture);
        });
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_when_lectureIsNotSavedInDb() {
        doReturn(false).when(lectureDao).existsById(lecture.getId());
        assertThrows(IllegalArgumentException.class, () -> {
            lectureService.delete(lecture);
        });
    }

    @Test
    void delete_shouldDeleteLecture_when_lectureIsValidAndExistsInDb() {
        doReturn(true).when(lectureDao).existsById(lecture.getId());

        lectureService.delete(lecture);

        verify(lectureDao).delete(lecture);
    }

    @Test
    void findForCourse_shouldThrowIllegalArgumentException_whenCourseNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            lectureService.findForCourse(null);
        });
    }

    @Test
    void findForCourse_shouldThrowIllegalArgumentException_whenCourseIsInvalid() {
        Course invalidCourse = new Course();
        assertThrows(IllegalArgumentException.class, () -> {
            lectureService.findForCourse(invalidCourse);
        });
    }

    @Test
    void findForCourse_shouldReturnLectures_whenCourseIsValid() {
        lectureService.findForCourse(course);
        verify(lectureDao).findByCourse(course);
    }

    @Test
    void findForCourseInTimeInterval_shouldThrowIllegalArgumentException_whenCourseNull() {
        LocalDateTime timeStart = LocalDateTime.of(2025, 1, 16, 0, 0);
        LocalDateTime timeEnd = LocalDateTime.of(2025, 1, 16, 23, 59);

        assertThrows(IllegalArgumentException.class, () -> {
            lectureService.findForCourseInTimeInterval(null, timeStart, timeEnd);
        });
    }

    @Test
    void findForCourseInTimeInterval_shouldThrowIllegalArgumentException_whenCourseIsInvalid() {
        Course invalidCourse = new Course();
        LocalDateTime timeStart = LocalDateTime.of(2025, 1, 16, 0, 0);
        LocalDateTime timeEnd = LocalDateTime.of(2025, 1, 16, 23, 59);

        assertThrows(IllegalArgumentException.class, () -> {
            lectureService.findForCourseInTimeInterval(invalidCourse, timeStart, timeEnd);
        });
    }

    @Test
    void findForCourseInTimeInterval_shouldThrowIllegalArgumentException_whenTimeStartAfterTimeEnd() {
        LocalDateTime timeStart = LocalDateTime.of(2025, 1, 16, 23, 59);
        LocalDateTime timeEnd = LocalDateTime.of(2025, 1, 16, 0, 0);

        assertThrows(IllegalArgumentException.class, () -> {
            lectureService.findForCourseInTimeInterval(course, timeStart, timeEnd);
        });
    }

    @Test
    void findForCourseInTimeInterval_shouldThrowIllegalArgumentException_whenTimeStartNull() {
        LocalDateTime timeEnd = LocalDateTime.of(2025, 1, 16, 23, 59);

        assertThrows(IllegalArgumentException.class, () -> {
            lectureService.findForCourseInTimeInterval(course, null, timeEnd);
        });
    }

    @Test
    void findForCourseInTimeInterval_shouldThrowIllegalArgumentException_whenTimeEndNull() {
        LocalDateTime timeStart = LocalDateTime.of(2025, 1, 16, 0, 0);

        assertThrows(IllegalArgumentException.class, () -> {
            lectureService.findForCourseInTimeInterval(course, timeStart, null);
        });
    }

    @Test
    void findForCourseInTimeInterval_shouldReturnLectures_whenCourseIsValidAndTimeIsValid() {
        LocalDateTime timeStart = LocalDateTime.of(2025, 1, 16, 0, 0);
        LocalDateTime timeEnd = LocalDateTime.of(2025, 1, 16, 23, 59);

        lectureService.findForCourseInTimeInterval(course, timeStart, timeEnd);

        verify(lectureDao).findByCourseAndTimeStartBetween(course, timeStart, timeEnd);
    }
}
