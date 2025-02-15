package ua.foxminded.mykyta.zemlianyi.university.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

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

    @BeforeEach
    void setUp() {
        Course anyCourse = new Course();

        lecture.setId(1L);
        lecture.setLectureType(LectureType.LABORATORIUM);
        lecture.setCourse(anyCourse);
        lecture.setTimeStart(LocalDateTime.of(2025, 1, 16, 0, 0));
        lecture.setTimeEnd(LocalDateTime.of(2025, 1, 16, 23, 59, 59));
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
}
